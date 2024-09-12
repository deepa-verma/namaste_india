package com.quinta.namasteindia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quinta.namasteindia.R
import com.quinta.namasteindia.response.StockOutProductScans


class ScanUpdateAdapter(private val listModel: List<StockOutProductScans>) : RecyclerView.Adapter<ScanUpdateAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_list_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        return holder.bindView(listModel[position],itemCount)
    }

    override fun getItemCount(): Int {
        return listModel.size
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var itemName: TextView = itemView.findViewById(R.id.tv_itemName)
        private var totalCrt: TextView = itemView.findViewById(R.id.tv_crt)
        private var totalCarton: TextView = itemView.findViewById(R.id.tv_cant)
        private var divider: View = itemView.findViewById(R.id.divider)

        fun bindView(listModel: StockOutProductScans, itemCount: Int) {
            itemName.text = listModel.materialDescription.toString()
            totalCrt.text = listModel.totalCarteQty.toString()
            totalCarton.text = listModel.scanCarteQty.toString()
            if(position==itemCount-1)
                divider.visibility = View.GONE
            else
                divider.visibility = View.VISIBLE
        }


    }

}