package com.quinta.namasteindia.adapter

import DeliveryData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quinta.namasteindia.R
import com.quinta.namasteindia.ui.ScanActivity

class DeliveryListAdapter(private val listModel: List<DeliveryData>) : RecyclerView.Adapter<DeliveryListAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
      return holder.bindView(listModel[position],itemCount)
    }

    override fun getItemCount(): Int {
       return listModel.size
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var deliveryNo: TextView = itemView.findViewById(R.id.tvDeliveryNo)
        private var quantity: TextView = itemView.findViewById(R.id.tvQuantity)
        private var vehicleNo: TextView = itemView.findViewById(R.id.tvVehicleNo)
        private val btScan: Button = itemView.findViewById(R.id.btScan)
        private val view1: View = itemView.findViewById(R.id.view1)


        fun bindView(listModel: DeliveryData, itemCount: Int) {
              deliveryNo.text = listModel.deliveryDocNo.toString()
              quantity.text = listModel.deliverQty.toString()
            vehicleNo.text = listModel.vehicleNo.toString()
            if(position==itemCount-1)
                view1.visibility = View.GONE
            else
                view1.visibility = View.VISIBLE

            if(listModel.deliveryStatus=="Pending")
                btScan.visibility=View.VISIBLE
            else
                btScan.visibility=View.GONE

            btScan.setOnClickListener(View.OnClickListener {
                val context = itemView.context

                val intent = Intent(context, ScanActivity::class.java)
                intent.putExtra("deliveryId", listModel.id.toString())
                intent.putExtra("deliveryQty", listModel.deliverQty.toString())
                intent.putExtra("vehicleNo", listModel.vehicleNo.toString())
                intent.putExtra("vehicleNo", listModel.vehicleNo.toString())
                intent.putExtra("position", position)
                context.startActivity(intent)
               // hitApi(context,listModel.id.toString(),listModel.deliverQty.toString())
            })
        }
    }

}


