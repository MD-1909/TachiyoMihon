package tachiyomi.presentation.core.components.material

import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Slider(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: IntProgression = 0..1,
    @IntRange(from = 0) steps: Int = with(valueRange) { (last - first) - 1 },
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val state = key(steps, valueRange) {
        rememberSliderState(
            value = value.toFloat(),
            steps = steps,
            trackRange = with(valueRange) { first.toFloat()..last.toFloat() },
        )
    }
    state.value = value.toFloat()
    Slider(
        state = state,
        modifier = modifier,
        enabled = enabled,
        onValueChange = { onValueChange(it.roundToInt()) },
        onValueChangeFinished = onValueChangeFinished,
        interactionSource = interactionSource,
        thumb = { LegacySliderThumb() },
        track = { LegacySliderTrack(it, vertical = false) },
    )
}

@Composable
fun LegacySliderThumb() {
    Spacer(Modifier.size(20.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
}

// Pre-expressive Material 3 slider track: thin line with dot ticks
@Composable
fun LegacySliderTrack(state: SliderState, vertical: Boolean) {
    val colors = MaterialTheme.colorScheme
    val activeTrack = colors.primary
    val inactiveTrack = colors.surfaceVariant
    val activeTick = colors.onPrimary.copy(alpha = 0.38f)
    val inactiveTick = colors.onSurfaceVariant.copy(alpha = 0.38f)
    val fraction = with(state.trackRange) {
        ((state.value - start) / (endInclusive - start)).coerceIn(0f, 1f)
    }
    Canvas(
        if (vertical) {
            Modifier.width(LegacyTrackHeight).fillMaxHeight()
        } else {
            Modifier.fillMaxWidth().height(LegacyTrackHeight)
        },
    ) {
        val reverse = !vertical && layoutDirection == LayoutDirection.Rtl
        fun point(f: Float): Offset {
            val p = if (reverse) 1f - f else f
            return if (vertical) Offset(center.x, size.height * p) else Offset(size.width * p, center.y)
        }
        val stroke = LegacyTrackHeight.toPx()
        drawLine(inactiveTrack, point(0f), point(1f), stroke, StrokeCap.Round)
        drawLine(activeTrack, point(0f), point(fraction), stroke, StrokeCap.Round)
        if (state.steps > 0) {
            val (active, inactive) = (0..state.steps + 1)
                .map { it.toFloat() / (state.steps + 1) }
                .partition { it <= fraction }
            drawPoints(active.map(::point), PointMode.Points, activeTick, 2.dp.toPx(), StrokeCap.Round)
            drawPoints(inactive.map(::point), PointMode.Points, inactiveTick, 2.dp.toPx(), StrokeCap.Round)
        }
    }
}

private val LegacyTrackHeight = 4.dp
