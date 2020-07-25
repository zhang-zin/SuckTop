package com.zj.sucktop

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class SuckTopDecoration : RecyclerView.ItemDecoration() {

    private val groupHeaderHeight: Float = dp2px(60f)

    private val headerPaint = Paint()
    private val textPaint = Paint()
    private val textRect = Rect()

    init {
        textPaint.color = Color.WHITE
        textPaint.textSize = 50f
        headerPaint.color = Color.GREEN
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.adapter is TestAdapter) {
            val testAdapter = parent.adapter as TestAdapter
            val childCount = parent.childCount

            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            for (index in 0 until childCount) {
                val child = parent.get(index)
                val position = parent.getChildAdapterPosition(child)

                if (child.top - groupHeaderHeight - parent.paddingTop >= 0)
                    if (testAdapter.isGroupHeader(position)) {
                        c.drawRect(
                            left.toFloat(), (child.top - groupHeaderHeight),
                            right.toFloat(), child.top.toFloat(), headerPaint
                        )
                        val groupName = testAdapter.data[position].groupName
                        textPaint.getTextBounds(
                            groupName, 0, groupName.length, textRect
                        )
                        c.drawText(
                            groupName,
                            (left + 20).toFloat(),
                            child.top - groupHeaderHeight / 2 + textRect.height() / 2,
                            textPaint
                        )
                    } else {
                        c.drawRect(
                            left.toFloat(), (child.top - 4).toFloat(),
                            right.toFloat(), child.top.toFloat(), headerPaint
                        )
                    }
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        if (parent.adapter is TestAdapter) {
            val testAdapter = parent.adapter as TestAdapter
            val linearLayoutManager = parent.layoutManager as LinearLayoutManager
            val position = linearLayoutManager.findFirstVisibleItemPosition()

            val itemView = parent.findViewHolderForAdapterPosition(position)?.itemView
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val top = parent.paddingTop

            val groupHeader = testAdapter.isGroupHeader(position + 1)
            if (groupHeader) {
                itemView?.run {
                    val bottom =
                        min(groupHeaderHeight.toInt(), itemView.bottom - top)
                    c.drawRect(
                        left.toFloat(), top.toFloat(), right.toFloat(),
                        (top + bottom).toFloat(), headerPaint
                    )
                    val groupName = testAdapter.data[position].groupName
                    textPaint.getTextBounds(
                        groupName, 0, groupName.length, textRect
                    )
                    c.clipRect(left, top, right, top + bottom)
                    c.drawText(
                        groupName, left + 20.toFloat(), top + bottom
                                - groupHeaderHeight / 2 + textRect.height() / 2.toFloat(), textPaint
                    )
                }
            } else {
                c.drawRect(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    top + groupHeaderHeight,
                    headerPaint
                )
                val groupName: String = testAdapter.data[position].groupName
                textPaint.getTextBounds(groupName, 0, groupName.length, textRect)
                c.drawText(
                    groupName,
                    left + 20.toFloat(),
                    top + groupHeaderHeight / 2 + (textRect.height() / 2).toFloat(),
                    textPaint
                )
            }
        }
    }

    /**
     * 空出item与item之间的位置
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.adapter is TestAdapter) {
            val testAdapter = parent.adapter as TestAdapter
            val groupHeader = testAdapter.isGroupHeader(parent.getChildAdapterPosition(view))
            if (groupHeader) {
                // 这一组的第一个
                outRect.set(0, groupHeaderHeight.toInt(), 0, 0)
            } else {
                outRect.set(0, 4, 0, 0)
            }
        }
    }

    fun dp2px(dpValue: Float): Float {
        val density = Resources.getSystem().displayMetrics.density
        return dpValue * density + 0.5f
    }

    fun px2dp(pxValue: Float): Float {
        val density = Resources.getSystem().displayMetrics.density
        return pxValue / density + 0.5f
    }

}