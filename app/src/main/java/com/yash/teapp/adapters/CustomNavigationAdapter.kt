package com.yash.teapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.yash.teapp.R
import com.yash.teapp.dataClasses.CustomMenuItem

class CustomNavigationAdapter(private val context: Context, private val items: List<CustomMenuItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.custom_nav_item, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = items[position]

        // Customize the view based on the item data
        viewHolder.iconImageView.setImageResource(item.iconResId)
        viewHolder.textView.text = item.text

        return view
    }

    private class ViewHolder(view: View) {
        val iconImageView: ImageView = view.findViewById(R.id.nav_item_icon)
        val textView: TextView = view.findViewById(R.id.nav_item_text)
    }


}
