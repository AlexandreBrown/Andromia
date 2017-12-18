package cstj.qc.ca.andromia.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.fragments.OnListItemFragmentInteractionListener

import cstj.qc.ca.andromia.helpers.SERVEUR_ANDROMIA_SERVICE
import cstj.qc.ca.andromia.models.Item
import kotlinx.android.synthetic.main.card_unit.view.*

/**
 * Created by Guillaume on 2017-12-12.
 */
class RecyclerViewAdapter(private val mValues:List<Item>, private val mListener: OnListItemFragmentInteractionListener?): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_unit, parent,false )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(mValues[position])
        holder.mView.setOnClickListener{
            mListener!!.onListItemFragmentInteraction(holder.item)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView:View):RecyclerView.ViewHolder(mView){
        var lblNameUnit = mView.lblNameUnit
        var lblNumberUnit = mView.lblNumberUnit
        var lblSetUnit = mView.lblSetUnit
        var imgUnit = mView.imgUnit
        var item: Item? = null

        fun bind(item: Item){
            val info = item.getAffichage()
            this.lblNameUnit.text = info[0]
            this.lblNumberUnit.text = info[3]
            this.lblSetUnit.text = info[2]
            this.item = item

            Picasso.with(imgUnit.context)
                    .load(info[1])
                    .resize(100,150)
                    .into(imgUnit)
        }
    }

}