package com.zj.sucktop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TestAdapter(val context: Context, var data: List<DataBean>) :
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_layout, null)
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv.text = data.get(position).name
    }

    fun isGroupHeader(position: Int): Boolean {
        return if (position == 0)
            true
        else {
            val currentGroupName = data[position].groupName // 当前组的名字
            val preGroupName = data[position - 1].groupName // 前一个组的名字
            currentGroupName != preGroupName  // 如果当前组和前一个组的名字不相同，则当前的为这个组的头
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tv)
    }

}