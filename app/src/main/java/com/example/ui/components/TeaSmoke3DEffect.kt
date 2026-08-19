package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * High-Definition 3D Volumetric Tea Smoke & Steam Effect
 * Simulates stereoscopic multi-layered rising aroma steam with sunlit depth shading,
 * 3D helical vortex eddies, volumetric vapor clouds, and micro aroma sparkles.
 */
@Composable
fun TeaSmoke3DOverlay(
    modifier: Modifier = Modifier,
    cupCenterXRatio: Float = 0.52f, // horizontal center of the tea cup rim in the image
    cupTopYRatio: Float = 0.58f,    // vertical position of the hot tea liquor surface
    steamIntensity: Float = 1.25f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "tea_smoke_3d_engine")

    // Multi-speed continuous cycle animations
    val cycleSlow by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycle_slow"
    )

    val cycleMedium by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycle_medium"
    )

    val cycleFast by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycle_fast"
    )

    // Interactive airflow disturbance
    var windShiftX by remember { mutableFloatStateOf(0f) }
    var windShiftY by remember { mutableFloatStateOf(0f) }

    // Seeded stable 3D particles & micro aroma droplets strictly constrained over the tea cup rim
    val volumetricPuffs = remember {
        List(28) { i ->
            SmokePuff3D(
                id = i,
                baseOffsetX = (Random.nextFloat() - 0.5f) * 32f, // Strictly centered over the tea cup opening
                speedMultiplier = 0.70f + Random.nextFloat() * 0.65f,
                depthZ = 0.20f + (i % 6) * 0.15f, // 0.20 (far) to 0.95 (close foreground)
                phaseShift = (i.toFloat() / 28f),
                baseRadius = 12f + Random.nextFloat() * 16f,
                curlFrequency = 1.4f + Random.nextFloat() * 1.8f,
                swirlSpeed = if (i % 2 == 0) 1.0f else -1.0f,
                sunlightCatch = Random.nextFloat() > 0.35f
            )
        }
    }

    val aromaGlints = remember {
        List(12) { i ->
            AromaGlint(
                id = i,
                offsetStart = (Random.nextFloat() - 0.5f) * 22f,
                speed = 0.85f + Random.nextFloat() * 0.5f,
                phase = i.toFloat() / 12f,
                size = 1.8f + Random.nextFloat() * 2.2f
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val originX = width * cupCenterXRatio
            val originY = height * cupTopYRatio
            val maxSteamHeight = originY * 0.96f

            // 1. BASE: Hot Tea Liquor Core Glow & Thermal Vapor Shimmer
            drawThermalCupGlow(
                originX = originX,
                originY = originY,
                cycle = cycleFast,
                intensity = steamIntensity
            )

            // 2. LAYER 1: Deep Backlit Volumetric Smoke Cloud (Soft sunlight atmospheric body)
            drawDeepVolumetricClouds(
                originX = originX,
                originY = originY,
                maxHeight = maxSteamHeight,
                cycle = cycleSlow,
                windX = windShiftX,
                intensity = steamIntensity
            )

            // 3. LAYER 2: 3D Helical Twisted Steam Vortex Ribbons (Braided core currents)
            draw3DHelicalSteamVortices(
                originX = originX,
                originY = originY,
                maxHeight = maxSteamHeight,
                cycle = cycleMedium,
                windX = windShiftX,
                intensity = steamIntensity
            )

            // 4. LAYER 3: Volumetric 3D Smoke Puffs with Z-Sorting & Sunlight Highlights
            draw3DVolumetricPuffs(
                puffs = volumetricPuffs,
                originX = originX,
                originY = originY,
                maxHeight = maxSteamHeight,
                globalCycle = cycleMedium,
                windX = windShiftX,
                intensity = steamIntensity
            )

            // 5. LAYER 4: Foreground High-Velocity Vapor Tendrils (Crisp hot steam wisps)
            drawForegroundHotTendrils(
                originX = originX,
                originY = originY,
                maxHeight = maxSteamHeight * 0.75f,
                cycle = cycleFast,
                windX = windShiftX,
                intensity = steamIntensity
            )

            // 6. LAYER 5: Golden Aroma Micro-Glints (Catching morning sunlight in tea garden)
            drawSunlitAromaGlints(
                glints = aromaGlints,
                originX = originX,
                originY = originY,
                maxHeight = maxSteamHeight,
                cycle = cycleMedium,
                intensity = steamIntensity
            )
        }
    }
}

/**
 * 3D Puff Data Structure with simulated stereoscopic Z-depth
 */
private data class SmokePuff3D(
    val id: Int,
    val baseOffsetX: Float,
    val speedMultiplier: Float,
    val depthZ: Float, // 0.0 (deep back background) to 1.0 (nearest foreground)
    val phaseShift: Float,
    val baseRadius: Float,
    val curlFrequency: Float,
    val swirlSpeed: Float,
    val sunlightCatch: Boolean
)

private data class AromaGlint(
    val id: Int,
    val offsetStart: Float,
    val speed: Float,
    val phase: Float,
    val size: Float
)

/**
 * Hot Thermal Cup Glow at black tea liquor surface
 */
private fun DrawScope.drawThermalCupGlow(
    originX: Float,
    originY: Float,
    cycle: Float,
    intensity: Float
) {
    val pulse = (sin(cycle * 6.28f) * 0.15f + 0.85f) * intensity
    val glowWidth = 52f * pulse
    val glowHeight = 14f * pulse

    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFFAF0).copy(alpha = 0.50f * pulse),
                Color(0xFFD97706).copy(alpha = 0.38f * pulse), // Rich amber black tea shimmer
                Color(0xFF78350F).copy(alpha = 0.15f * pulse),
                Color(0x00FFFFFF)
            ),
            center = Offset(originX, originY),
            radius = glowWidth
        ),
        topLeft = Offset(originX - glowWidth, originY - glowHeight),
        size = Size(glowWidth * 2, glowHeight * 2)
    )
}

/**
 * Deep Volumetric 3D Smoke Clouds (Rising strictly from the tea cup)
 */
private fun DrawScope.drawDeepVolumetricClouds(
    originX: Float,
    originY: Float,
    maxHeight: Float,
    cycle: Float,
    windX: Float,
    intensity: Float
) {
    val cloudCount = 4
    for (i in 0 until cloudCount) {
        val puffPhase = (cycle + i.toFloat() / cloudCount) % 1f
        val currentY = originY - (puffPhase * maxHeight)
        val progress = puffPhase // 0 = at cup, 1 = top

        // 3D perspective dilation: puffs expand as they ascend into the air
        val expand = 1.0f + progress * 3.2f
        val sway = sin((puffPhase * 6.28f * 1.2f) + windX + (i * 1.5f)) * (16f * expand)
        val puffCenterX = originX + sway

        val alpha = when {
            progress < 0.12f -> (progress / 0.12f) * 0.28f
            progress > 0.60f -> ((1f - progress) / 0.40f) * 0.28f
            else -> 0.28f
        } * intensity

        val radX = 26f * expand
        val radY = 22f * expand

        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFFBF2).copy(alpha = alpha),
                    Color(0xFFF7ECE1).copy(alpha = alpha * 0.60f),
                    Color(0x00FFFFFF)
                ),
                center = Offset(puffCenterX, currentY),
                radius = radX
            ),
            topLeft = Offset(puffCenterX - radX, currentY - radY),
            size = Size(radX * 2, radY * 2)
        )
    }
}

/**
 * 3D Helical Twisted Steam Vortex Ribbons
 */
private fun DrawScope.draw3DHelicalSteamVortices(
    originX: Float,
    originY: Float,
    maxHeight: Float,
    cycle: Float,
    windX: Float,
    intensity: Float
) {
    // 3 distinct streams creating a 3D triple-helix rising pattern
    val streamConfigs = listOf(
        Triple(-16f, 1.00f, 26f),
        Triple(6f, 0.85f, -28f),
        Triple(20f, 1.15f, 32f)
    )

    streamConfigs.forEachIndexed { idx, (startOffsetX, speed, ampX) ->
        val path = Path()
        val segments = 28
        val streamPhase = (cycle * speed + idx * 0.33f) % 1f
        var isFirst = true

        for (step in 0..segments) {
            val t = step.toFloat() / segments
            val currentY = originY - (t * maxHeight)

            // 3D perspective angle & depth oscillation
            val angle = (t * 2.5f * 6.28f) + (streamPhase * 6.28f) + windX
            val depthZ = sin(angle) * 0.5f + 0.5f // 0 (back) to 1 (front)
            val swayX = cos(angle) * (ampX * (0.35f + t * 1.65f))
            val currentX = originX + (startOffsetX * (1f - t * 0.4f)) + swayX

            if (isFirst) {
                path.moveTo(currentX, currentY)
                isFirst = false
            } else {
                path.lineTo(currentX, currentY)
            }
        }

        val ribbonAlpha = (0.36f * intensity)
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0x00FFFFFF),
                    Color(0xFFFFFFFF).copy(alpha = ribbonAlpha * 0.7f),
                    Color(0xFFFFF9EE).copy(alpha = ribbonAlpha),
                    Color(0xFFFFFFFF).copy(alpha = ribbonAlpha * 0.8f),
                    Color(0x00FFFFFF)
                ),
                startY = originY,
                endY = originY - maxHeight
            ),
            style = Stroke(
                width = 6.5f + idx * 1.5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

/**
 * 3D Volumetric Puffs with Z-Sorting, volumetric depth, and sunlit illumination
 */
private fun DrawScope.draw3DVolumetricPuffs(
    puffs: List<SmokePuff3D>,
    originX: Float,
    originY: Float,
    maxHeight: Float,
    globalCycle: Float,
    windX: Float,
    intensity: Float
) {
    // Sort by depthZ for true stereoscopic 3D occlusion
    val sorted = puffs.sortedBy { it.depthZ }

    sorted.forEach { puff ->
        val progress = (globalCycle * puff.speedMultiplier + puff.phaseShift) % 1f
        val currentY = originY - (progress * maxHeight)

        // Perspective dynamics based on depthZ
        val zScale = 0.55f + (puff.depthZ * 0.85f) // foreground is larger
        val dynamicWave = sin((progress * puff.curlFrequency * 6.28f) + windX + puff.phaseShift * 12f)
        val sway = dynamicWave * (22f * (1f + progress * 2.2f)) * puff.swirlSpeed
        val currentX = originX + (puff.baseOffsetX * (1f + progress * 1.6f)) + sway

        val alphaCurve = when {
            progress < 0.10f -> progress / 0.10f
            progress > 0.65f -> (1f - progress) / 0.35f
            else -> 1f
        }

        val baseAlpha = (alphaCurve * 0.38f * puff.depthZ * intensity).coerceIn(0f, 1f)
        val radius = puff.baseRadius * zScale * (1f + progress * 2.0f)

        // 3D Sunlit shading: Sun comes from top-left in the tea garden photo
        // Create an offset highlight on the upper-left of each puff
        val highlightOffset = Offset(currentX - radius * 0.25f, currentY - radius * 0.25f)

        val puffColors = if (puff.sunlightCatch && puff.depthZ > 0.45f) {
            listOf(
                Color(0xFFFFFDF5).copy(alpha = baseAlpha * 1.15f),
                Color(0xFFFFEDB3).copy(alpha = baseAlpha * 0.75f), // Warm golden sun glint
                Color(0x00FFFFFF)
            )
        } else {
            listOf(
                Color(0xFFFFFFFF).copy(alpha = baseAlpha),
                Color(0xFFF3EAE0).copy(alpha = baseAlpha * 0.6f),
                Color(0x00FFFFFF)
            )
        }

        drawCircle(
            brush = Brush.radialGradient(
                colors = puffColors,
                center = highlightOffset,
                radius = radius
            ),
            radius = radius,
            center = Offset(currentX, currentY)
        )
    }
}

/**
 * Foreground High-Velocity Hot Steam Tendrils (crisp wisps rising directly from rim)
 */
private fun DrawScope.drawForegroundHotTendrils(
    originX: Float,
    originY: Float,
    maxHeight: Float,
    cycle: Float,
    windX: Float,
    intensity: Float
) {
    val tendrilOffsets = listOf(-12f, -3f, 7f, 16f)
    tendrilOffsets.forEachIndexed { i, offset ->
        val phase = (cycle + i * 0.25f) % 1f
        val wispHeight = maxHeight * (0.6f + (i % 2) * 0.3f)
        val startY = originY
        val endY = originY - (phase * wispHeight)

        val alpha = when {
            phase < 0.15f -> phase / 0.15f
            phase > 0.75f -> (1f - phase) / 0.25f
            else -> 1f
        } * 0.42f * intensity

        val path = Path().apply {
            moveTo(originX + offset, startY)
            val cp1X = originX + offset + sin(phase * 6.28f + i + windX) * 16f
            val cp1Y = startY - (wispHeight * 0.35f)
            val cp2X = originX + offset - cos(phase * 6.28f + i + windX) * 22f
            val cp2Y = startY - (wispHeight * 0.75f)
            val endX = originX + offset + sin(phase * 6.28f + i) * 28f
            cubicTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY)
        }

        drawPath(
            path = path,
            color = Color.White.copy(alpha = alpha),
            style = Stroke(
                width = 3.2f,
                cap = StrokeCap.Round
            )
        )
    }
}

/**
 * Golden Aroma Micro-Glints
 */
private fun DrawScope.drawSunlitAromaGlints(
    glints: List<AromaGlint>,
    originX: Float,
    originY: Float,
    maxHeight: Float,
    cycle: Float,
    intensity: Float
) {
    glints.forEach { g ->
        val prog = (cycle * g.speed + g.phase) % 1f
        val y = originY - (prog * maxHeight * 0.85f)
        val wave = sin(prog * 6.28f * 2.5f + g.id) * 18f
        val x = originX + (g.offsetStart * (1f + prog * 1.5f)) + wave

        val alpha = when {
            prog < 0.15f -> prog / 0.15f
            prog > 0.80f -> (1f - prog) / 0.20f
            else -> 1f
        } * 0.75f * intensity

        // Draw micro star glint
        drawCircle(
            color = Color(0xFFFFDF7D).copy(alpha = alpha),
            radius = g.size,
            center = Offset(x, y)
        )
    }
}
