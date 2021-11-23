package com.pay.workoutapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pay.workoutapp.R
import com.pay.workoutapp.databinding.ItemViewBinding
import com.pay.workoutapp.model.Model

class ExerciseStatusAdapter(val items: ArrayList<Model>, val context: Context) :
    RecyclerView.Adapter<ExerciseStatusAdapter.StatusViewHolder>() {


    class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_item = ItemViewBinding.bind(view).tvStatusItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val model: Model = items[position]
        holder.tv_item.text = model.id.toString()

        if (model.isSelected) {
            holder.tv_item.setBackgroundResource(R.drawable.item_circular_color_gray_background_selected)
            holder?.tv_item?.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else if (model.isCompleted) {
            holder?.tv_item?.setBackgroundResource(R.drawable.item_circular_color_gray_background_finished)
            holder?.tv_item?.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder?.tv_item?.setBackgroundResource(R.drawable.item_circular_color_gray_background)
            holder?.tv_item?.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}