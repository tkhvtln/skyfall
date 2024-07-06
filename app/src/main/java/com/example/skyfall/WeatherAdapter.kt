package com.example.skyfall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skyfall.databinding.NextDayInfoBinding

class WeatherAdapter(private val itemClickListener: ItemClickListener): ListAdapter<WeatherData, WeatherAdapter.MyViewHolder>(Comparator()) {

    private var selectedIndex = -1;

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = NextDayInfoBinding.bind(view)

        fun bind(item: WeatherData, itemClickListener: ItemClickListener, indexDay: Int, isSelected: Boolean) {
            binding.imgWeather.setImageResource(item.condition)
            binding.tvTemperature.text = item.temperature
            binding.item.setOnClickListener() {
                itemClickListener.onItemClickListener(indexDay)
            }

            if (isSelected) {
                binding.item.setBackgroundResource(R.drawable.day_selected_shape)
            } else {
                binding.item.setBackgroundResource(R.drawable.day_unselected_shape)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.next_day_info, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val isSelected = position == selectedIndex
        holder.bind(getItem(position), itemClickListener, position, isSelected)
    }

    class Comparator: DiffUtil.ItemCallback<WeatherData>() {
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return  oldItem == newItem
        }
    }

    interface ItemClickListener {
        fun onItemClickListener(indexDay: Int)
    }

    fun updateSelectedIndex(newIndex: Int) {
        val oldIndex = selectedIndex
        selectedIndex = newIndex
        notifyItemChanged(oldIndex)
        notifyItemChanged(newIndex)
    }
}