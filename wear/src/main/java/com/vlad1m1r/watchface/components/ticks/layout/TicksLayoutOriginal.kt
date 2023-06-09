package com.vlad1m1r.watchface.components.ticks.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.wear.watchface.DrawMode
import com.vlad1m1r.watchface.R
import com.vlad1m1r.watchface.components.ticks.TickPaintProvider
import com.vlad1m1r.watchface.components.ticks.usecase.AdjustTicks
import com.vlad1m1r.watchface.components.ticks.usecase.RoundCorners
import com.vlad1m1r.watchface.data.state.TicksState
import com.vlad1m1r.watchface.model.Point
import com.vlad1m1r.watchface.utils.getLighterGrayscale
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class TicksLayoutOriginal @Inject constructor(
    @ApplicationContext context: Context,
    override val tickPaintProvider: TickPaintProvider,
    override val adjustTicks: AdjustTicks,
    override val roundCorners: RoundCorners
) : TicksLayout(context) {

    private val tickLength = context.resources.getDimension(R.dimen.original_tick_length)
    private var watchTickColor = 0
    private val tickWidth = context.resources.getDimension(R.dimen.original_tick_width)

    private val tickBurnInPadding = context.resources.getDimension(R.dimen.original_tick_padding)
    private var tickPadding = tickBurnInPadding

    private lateinit var tickPaint: Paint

    private var outerTickRadius: Float = 0f
    private var innerTickRadius: Float = 0f

    override fun draw(canvas: Canvas, drawMode: DrawMode, center: Point) {

        this.outerTickRadius = center.x - tickPadding
        this.innerTickRadius = center.x - tickLength - tickPadding

        tickPaint.apply {
            if (drawMode == DrawMode.AMBIENT) {
                tickPaintProvider.inAmbientMode(
                    this,
                    getLighterGrayscale(watchTickColor),
                    state.useAntialiasingInAmbientMode
                )
            } else {
                tickPaintProvider.inInteractiveMode(this, watchTickColor)
            }
        }
        tickPadding = if (shouldAdjustForBurnInProtection(drawMode)) {
            tickBurnInPadding
        } else {
            0f
        }


        for (tickIndex in 0..11) {
            val tickRotation = tickIndex * PI / 6
            val adjust = adjustTicks(
                tickRotation,
                center,
                bottomInset,
                isSquareScreen,
                state.shouldAdjustToSquareScreen
            )
            val roundCorners = if (state.shouldAdjustToSquareScreen) roundCorners(
                tickRotation,
                center,
                PI / 20
            ) * 10 else 0.0

            val innerX = sin(tickRotation) * (innerTickRadius - roundCorners) * adjust
            val innerY = -cos(tickRotation) * (innerTickRadius - roundCorners) * adjust
            val outerX = sin(tickRotation) * (outerTickRadius - roundCorners) * adjust
            val outerY = -cos(tickRotation) * (outerTickRadius - roundCorners) * adjust
            canvas.drawLine(
                (center.x + innerX).toFloat(), (center.y + innerY).toFloat(),
                (center.x + outerX).toFloat(), (center.y + outerY).toFloat(), tickPaint
            )
        }
    }

    override fun setTicksState(ticksState: TicksState) {
        super.setTicksState(ticksState)
        this.watchTickColor = state.hourTicksColor
        this.tickPaint = tickPaintProvider.getTickPaint(watchTickColor, tickWidth)
    }
}