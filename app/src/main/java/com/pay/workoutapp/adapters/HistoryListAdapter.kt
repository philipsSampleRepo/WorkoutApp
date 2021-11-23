package com.pay.workoutapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pay.workoutapp.R
import com.pay.workoutapp.databinding.HistoryItemListViewBinding
import com.pay.workoutapp.db.DBEntity
import com.pay.workoutapp.utils.BMIHistoryItemsListener

class HistoryListAdapter(
    val context: Context,
    val customItemListener: BMIHistoryItemsListener?
) :
    RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>() {

    private var itemsList: ArrayList<DBEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.history_item_list_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val model: DBEntity = itemsList[position]

        holder.tv_date.text = model.date
        holder.tv_bmi.text = model.bmi_value
        holder.tv_description.text = model.description
        holder.ib_edit.setOnClickListener {
            customItemListener?.let {
                it.onUpdate(model, position)
            }
        }
        holder.ib_delete.setOnClickListener {
            customItemListener?.let {
                it.onDelete(model, position)
            }
        }

        if (position % 2 == 0) {
            holder.cl_history.setBackgroundResource(R.drawable.history_listitem_background_selected)
        } else {
            holder.cl_history.setBackgroundResource(R.drawable.history_listitem_background)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_date = HistoryItemListViewBinding.bind(view).tvDate
        val tv_bmi = HistoryItemListViewBinding.bind(view).tvBmiItemVal
        val tv_description = HistoryItemListViewBinding.bind(view).tvBmiItemDes
        val ib_edit = HistoryItemListViewBinding.bind(view).ivItemEdit
        val ib_delete = HistoryItemListViewBinding.bind(view).ivItemDelete
        val cl_history = HistoryItemListViewBinding.bind(view).cvHistory
    }

    fun removeItem(position: Int) {
        itemsList.removeAt(position)
        notifyDataSetChanged()
    }

    fun updateItem(entity: DBEntity, position: Int) {
        itemsList.set(position, entity)
        notifyDataSetChanged()
    }

    fun setItems(historyItemsList: List<DBEntity>) {
        itemsList.clear()
        itemsList.addAll(historyItemsList)
        notifyDataSetChanged()
    }
}