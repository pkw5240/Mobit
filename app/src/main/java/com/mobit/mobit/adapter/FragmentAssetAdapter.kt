package com.mobit.mobit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobit.mobit.R
import com.mobit.mobit.data.CoinAsset
import java.math.RoundingMode
import java.text.DecimalFormat

/*
FragmentAsset에서 보유 자산을 보여줄 때 사용하는 adapter 입니다.
*/
class FragmentAssetAdapter(var items: ArrayList<CoinAsset>) :
    RecyclerView.Adapter<FragmentAssetAdapter.ViewHolder>() {

    val intFormatter = DecimalFormat("###,###").apply {
        this.roundingMode = RoundingMode.DOWN
    }
    val doubleFormatter2 = DecimalFormat("###,###.##").apply {
        this.roundingMode = RoundingMode.DOWN
    }
    val doubleFormatter4 = DecimalFormat("###,###.####").apply {
        this.roundingMode = RoundingMode.DOWN
    }

    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(code: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView
        val codeView: TextView
        val gainAndLossView: TextView
        val yieldView: TextView
        val retainedNumView: TextView
        val averagePriceView: TextView
        val evaluationView: TextView
        val buyPriceView: TextView

        init {
            nameView = itemView.findViewById(R.id.nameView)
            codeView = itemView.findViewById(R.id.codeView)
            gainAndLossView = itemView.findViewById(R.id.gainAndLossView)
            yieldView = itemView.findViewById(R.id.yieldView)
            retainedNumView = itemView.findViewById(R.id.retainedNumView)
            averagePriceView = itemView.findViewById(R.id.averagePriceView)
            evaluationView = itemView.findViewById(R.id.evaluationView)
            buyPriceView = itemView.findViewById(R.id.buyPriceView)

            itemView.setOnClickListener {
                listener?.onItemClicked(items[adapterPosition].code)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_asset_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = items[position].name
        val code = items[position].code.split("-")[1]
        holder.itemView.context.apply {
            holder.codeView.text = getString(R.string.asset_code_string, code)
            holder.retainedNumView.text = getString(
                R.string.asset_two_string,
                doubleFormatter4.format(items[position].number),
                code
            )
            holder.averagePriceView.text =
                if (items[position].averagePrice > 100.0)
                    getString(
                        R.string.asset_price_string,
                        intFormatter.format(items[position].averagePrice)
                    )
                else
                    getString(
                        R.string.asset_price_string,
                        doubleFormatter2.format(items[position].averagePrice)
                    )
            holder.evaluationView.text =
                getString(R.string.asset_price_string, intFormatter.format(items[position].amount))

            val buyPrice = items[position].averagePrice * items[position].number
            holder.buyPriceView.text =
                getString(R.string.asset_price_string, intFormatter.format(buyPrice))

            val gainAndLoss = items[position].amount - buyPrice
            val yieldValue = gainAndLoss / buyPrice * 100
            holder.gainAndLossView.text = intFormatter.format(gainAndLoss)
            holder.yieldView.text =
                getString(R.string.asset_yield_string, doubleFormatter2.format(yieldValue))

            val rgb = if (gainAndLoss > 0) Color.rgb(
                207,
                80,
                71
            ) else if (gainAndLoss < 0) Color.rgb(
                25,
                96,
                186
            ) else Color.rgb(211, 212, 214)
            holder.gainAndLossView.setTextColor(rgb)
            holder.yieldView.setTextColor(rgb)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}