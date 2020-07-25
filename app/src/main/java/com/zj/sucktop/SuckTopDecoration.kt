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

    /**
     * 绘制跟随滑动的头部
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.adapter is TestAdapter) {
            val testAdapter = parent.adapter as TestAdapter
            val childCount = parent.childCount

            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            for (index in 0 until childCount) {
                val view = parent[index]
                val position = parent.getChildAdapterPosition(view)

                if (view.top - parent.paddingTop - groupHeaderHeight >= 0) {
                    // 需要考虑recycleView的paddingTop，recycleView滑出时不需要绘制
                    val groupName = testAdapter.data[position].groupName
                    if (testAdapter.isGroupHeader(position)) {
                        c.drawRect(
                            left.toFloat(), view.top - groupHeaderHeight,
                            right.toFloat(), view.top.toFloat(), headerPaint
                        )
                        textPaint.getTextBounds(groupName, 0, groupName.length, textRect)
                        c.drawText(
                            groupName,
                            (left + 20).toFloat(),
                            view.top - groupHeaderHeight / 2 + textRect.height() / 2,
                            textPaint
                        )
                    } else {
                        c.drawRect(
                            left.toFloat(), (view.top - 4).toFloat(),
                            right.toFloat(), view.top.toFloat(), headerPaint
                        )
                    }
                }
            }
        }
    }

    /**
     * 绘制悬停的头部
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        if (parent.adapter is TestAdapter) {
            val testAdapter = parent.adapter as TestAdapter
            val linearLayoutManager = parent.layoutManager as LinearLayoutManager

            // 当前可见recycleView的第一个item
            val position = linearLayoutManager.findFirstVisibleItemPosition()

            val itemView = parent.findViewHolderForAdapterPosition(position)?.itemView
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val top = parent.paddingTop

            // 第一个可见的item，被悬停头部挡住的时候，其实还在
            val groupHeader = testAdapter.isGroupHeader(position + 1)
            val groupName = testAdapter.data[position].groupName
            if (groupHeader) {
                itemView?.run {
                    val bottom =
                        min(groupHeaderHeight.toInt(), itemView.bottom - top)
                    c.drawRect(
                        left.toFloat(), top.toFloat(), right.toFloat(),
                        (top + bottom).toFloat(), headerPaint
                    )
                    textPaint.getTextBounds(
                        groupName, 0, groupName.length, textRect
                    )
                    // 在视觉上文字也需要移动，裁剪画布和rect大小一样
                    c.clipRect(left, top, right, top + bottom)
                    c.drawText(
                        groupName,
                        left + 20.toFloat(),
                        top + bottom - groupHeaderHeight / 2 + textRect.height() / 2.toFloat(),
                        textPaint
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
     * 空出item与item之间的位置，item与item之间的间隔
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
                // 这一组的第一个，头部需要悬停的间隔
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