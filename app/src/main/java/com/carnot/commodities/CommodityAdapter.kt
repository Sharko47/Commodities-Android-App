package com.carnot.commodities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.carnot.commodities.data.model.Record
import com.carnot.commodities.databinding.CommodityListItemBinding
import com.carnot.commodities.utils.convertToCurrency

class CommodityAdapter(private val commodityList: List<Record>) :
    RecyclerView.Adapter<CommodityAdapter.CommodityViewHolder>() {

//    private val differCallback = object : DiffUtil.ItemCallback<Record>() {
//        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
//            return oldItem.commodity == newItem.commodity
//        }
//
//        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(this, differCallback)

    inner class CommodityViewHolder(private val binding: CommodityListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Record) {
            binding.commodityState.text = "State : ${item.state}"
            binding.commodityDistrict.text = "District : ${item.district}"
            binding.commodityMarket.text = "Market : ${item.market}"
            binding.commodityVariety.text = "Variety : ${item.variety}"
            binding.commodityName.text = "Commodity : ${item.commodity}"
            binding.commodityArrivalDate.text = "Arrival Date : ${item.arrivalDate}"
            if (!item.modalPrice.isNullOrBlank()) {
                binding.commodityPrice.text = item.modalPrice.toInt().convertToCurrency()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommodityViewHolder {
        return CommodityViewHolder(CommodityListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CommodityViewHolder, position: Int) {
        with(commodityList[position]) {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int {
        return commodityList.size
    }
}