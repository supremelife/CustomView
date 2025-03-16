package com.gentle.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.withStyledAttributes
import androidx.core.util.TypedValueCompat.dpToPx
import com.gentle.customview.R

class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @IntRange(from = 0)
    var angle: Int = 120 // 默认圆心角为 120 度
        set(value) {
            field = value
            invalidate()
        }

    @FloatRange(from = 0.toDouble(), fromInclusive = false)
    var maxValue: Float = 30f // 默认最大值 30
        set(value) {
            field = value
            invalidate()
        }

    @FloatRange(from = 0.toDouble())
    var progress: Float = 25f // 默认当前进度值 25
        set(value) {
            field = value
            invalidate()
        }

    var baseColor: Int = 0xFFF0F0F0.toInt() // 默认基础扇形颜色为绿色
        set(value) {
            field = value
            basePaint.color = value
            invalidate()
        }

    var highlightColor: Int = 0xFF1DF0BB.toInt() // 默认高亮扇形颜色为蓝色
        set(value) {
            field = value
            highlightPaint.color = value
            invalidate()
        }

    var labelLineColor: Int = 0x33A8B2BD // 默认线条颜色为灰色
        set(value) {
            field = value
            labelLinePaint.color = value
            invalidate()
        }

    var labelTextColor: Int = 0xFF101010.toInt() // 默认文本颜色为灰色
        set(value) {
            field = value
            labelTextPaint.color = value
            invalidate()
        }

    @FloatRange(from = 0.toDouble())
    var labelTextSize: Float = dpToPx(context, 12f) // 默认文本大小为 30sp
        set(value) {
            field = value
            labelTextPaint.textSize = value
            invalidate()
        }

    var showLabel: Boolean = true // 默认显示label
        set(value) {
            field = value
            invalidate()
        }

    @IntRange(from = 1)
    var labelNum: Int = 3 // 默认label数量 3
        set(value) {
            field = value
            invalidate()
        }

    var showAngleText: Boolean = true // 默认显示文本
        set(value) {
            field = value
            invalidate()
        }

    private val basePaint = Paint().apply {
        isAntiAlias = true // 抗锯齿
        color = baseColor
        style = Paint.Style.FILL // 设置填充样式
    }

    private val highlightPaint = Paint().apply {
        isAntiAlias = true // 抗锯齿
        color = highlightColor
        style = Paint.Style.FILL // 设置填充样式
    }

    private val labelLinePaint = Paint().apply {
        isAntiAlias = true // 抗锯齿
        color = labelLineColor // 灰色
        style = Paint.Style.STROKE // 设置为线条样式
        strokeWidth = dpToPx(context, 1f) // 设置线条宽度
    }

    private val labelTextPaint = Paint().apply {
        isAntiAlias = true // 抗锯齿
        color = labelTextColor // 灰色
        textSize = labelTextSize // 设置文本大小
    }
    private var labelUnit = "ft"

    private var textBoundsRect = Rect()
    private val angleTextMargin = dpToPx(context, 10f)
    private val labelTextMargin = dpToPx(context, 4f)

    init {
        attrs?.let {
            context.withStyledAttributes(it, R.styleable.PieChartView, defStyleAttr, 0) {
                angle = getInteger(R.styleable.PieChartView_angle, 120)
                maxValue = getFloat(R.styleable.PieChartView_maxValue, 30f)
                progress = getFloat(R.styleable.PieChartView_progress, 25f)
                baseColor =
                    getColor(R.styleable.PieChartView_baseColor, 0xFFF0F0F0.toInt())
                highlightColor = getColor(
                    R.styleable.PieChartView_highlightColor,
                    0xFF1DF0BB.toInt()
                )
                showLabel = getBoolean(R.styleable.PieChartView_showLabel, true)
                labelNum = getInteger(R.styleable.PieChartView_labelNum, 3)
                labelUnit = getString(R.styleable.PieChartView_labelUnit) ?: "ft"
                labelLineColor = getColor(R.styleable.PieChartView_labelLineColor, 0x33A8B2BD)
                labelTextColor =
                    getColor(R.styleable.PieChartView_labelTextColor, 0xFF101010.toInt())
                labelTextSize =
                    getDimension(R.styleable.PieChartView_labelTextSize, dpToPx(context, 12f))
                showAngleText =
                    getBoolean(R.styleable.PieChartView_showAngleText, true)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val chartWidth = (width - paddingStart - paddingEnd).toFloat()
        val chartHeight: Float = if (showAngleText) {
            height - paddingTop - paddingBottom - angleTextMargin - labelTextSize
        } else {
            (height - paddingTop - paddingBottom).toFloat()
        }

        val chartAngle = angle.toFloat()
        // 计算半径
        val radius = if (chartAngle <= 180f) {
            (chartWidth / 2).coerceAtMost(chartHeight)
        } else {
            chartWidth.coerceAtMost(chartHeight) / 2 // 取宽高较小值的一半作为半径
        }

        // 设置扇形的中心位置
        val centerX = chartWidth / 2 + paddingStart
        val centerY = paddingTop + radius

        // 创建一个矩形区域，用于定义基础扇形的边界
        val baseRectF =
            RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // 计算起始角度
        val startAngle = -90f - chartAngle / 2

        // 绘制基础扇形
        canvas.drawArc(baseRectF, startAngle, chartAngle, true, basePaint)

        // 计算高亮扇形的半径
        val highlightRadius = radius * (progress / maxValue)

        // 创建一个矩形区域，用于定义高亮扇形的边界
        val highlightRectF = RectF(
            centerX - highlightRadius,
            centerY - highlightRadius,
            centerX + highlightRadius,
            centerY + highlightRadius
        )

        // 绘制高亮扇形
        canvas.drawArc(highlightRectF, startAngle, chartAngle, true, highlightPaint)

        // 绘制灰色弧形线条和标注文本

        if (showLabel) {
            for (index in 1..labelNum) {
                val lineRadius = radius * index / labelNum
                val label = (maxValue * index / labelNum).printFormat() + labelUnit

                if (index != labelNum) {
                    val lineRectF = RectF(
                        centerX - lineRadius,
                        centerY - lineRadius,
                        centerX + lineRadius,
                        centerY + lineRadius
                    )
                    canvas.drawArc(lineRectF, startAngle, chartAngle, false, labelLinePaint)
                }

                // 计算文本位置
                labelTextPaint.getTextBounds(label, 0, label.length, textBoundsRect)
                val textWidth = textBoundsRect.width()
                val textHeight = textBoundsRect.height()

                val textX = centerX - textWidth / 2f
                val textY = centerY - lineRadius + textHeight + labelTextMargin

                // 绘制文本
                canvas.drawText(label, textX, textY, labelTextPaint)
            }
//            val lineRadii = listOf(radius / 3f, 2 * radius / 3f, radius)
//            val labels = listOf("10ft", "20ft", "30ft")
//            for ((index, lineRadius) in lineRadii.withIndex()) {
//                if (index != lineRadii.lastIndex) {
//                    val lineRectF = RectF(
//                        centerX - lineRadius,
//                        centerY - lineRadius,
//                        centerX + lineRadius,
//                        centerY + lineRadius
//                    )
//                    canvas.drawArc(lineRectF, startAngle, chartAngle, false, labelLinePaint)
//                }
//
//                // 计算文本位置
//                labelTextPaint.getTextBounds(labels[index], 0, labels[index].length, textBoundsRect)
//                val textWidth = textBoundsRect.width()
//                val textHeight = textBoundsRect.height()
//
//                val textX = centerX - textWidth / 2f
//                val textY = centerY - lineRadius + textHeight + labelTextMargin
//
//                // 绘制文本
//                canvas.drawText(labels[index], textX, textY, labelTextPaint)
//            }
        }

        if (showAngleText) {
            // 在圆心的正下方10dp处显示圆心角的度数
            val angleText = "$angle°"
            val angleTextBounds = textBoundsRect
            labelTextPaint.getTextBounds(angleText, 0, angleText.length, angleTextBounds)
            val angleTextWidth = angleTextBounds.width()
            val angleTextHeight = angleTextBounds.height()

            val angleTextX = centerX - angleTextWidth / 2f
            val angleTextY = centerY + angleTextMargin + angleTextHeight / 2f

            canvas.drawText(angleText, angleTextX, angleTextY, labelTextPaint)
        }
    }

    private fun Float.printFormat(): String {
        return if (this % 1 == 0f) {
            // 如果小数部分为零，转换为整数
            this.toInt().toString()
        } else {
            // 否则，保留为浮点数
            this.toString()
        }
    }

    private fun dpToPx(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

}
