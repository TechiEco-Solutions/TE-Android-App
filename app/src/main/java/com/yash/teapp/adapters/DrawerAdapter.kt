package com.anshita.teapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yash.teapp.dataClasses.DrawerItem
import com.yash.teapp.databinding.DrawerItemBinding

class DrawerAdapter(private val items: List<DrawerItem>) :
    RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder>() {

    class DrawerViewHolder(val binding : DrawerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DrawerItem) {
            binding.drawerItemTitle.text = item.title
            binding.drawerItemIcon.setImageResource(item.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerViewHolder {
        val binding = DrawerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DrawerViewHolder(binding)

    }

    override fun onBindViewHolder(holder: DrawerViewHolder, position: Int) {
        holder.bind(items[position])
        holder.binding.drawerListItem.setOnClickListener {
            onClick?.invoke(position)
        }

    }

    override fun getItemCount(): Int = items.size

    var onClick : ((Int) -> Unit)? = null
        get() = field

}


