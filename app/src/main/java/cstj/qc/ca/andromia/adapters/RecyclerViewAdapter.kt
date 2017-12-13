package cstj.qc.ca.andromia.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import com.squareup.picasso.Picasso
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.fragments.OnListItemFragmentInteractionListener

import cstj.qc.ca.andromia.fragments.UnitsExplorateurFragment
import cstj.qc.ca.andromia.helpers.SERVEUR_ANDROMIA_SERVICE
import cstj.qc.ca.andromia.models.Unit
import kotlinx.android.synthetic.main.card_unit.view.*

/**
 * Created by Guillaume on 2017-12-12.
 */
class RecyclerViewAdapter(private val mValues:List<Unit>, private val mListener: OnListItemFragmentInteractionListener?): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_unit, parent,false )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(mValues[position])
        holder.mView.setOnClickListener{
            mListener!!.onListItemFragmentInteraction(holder.unit)
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
        var unit: Unit? = null

        fun bind(unit: Unit){
            this.unit = unit
            this.lblNameUnit.text = unit.name
            this.lblNumberUnit.text = unit.number
            this.lblSetUnit.text = unit.set

            val urlUnit = "$SERVEUR_ANDROMIA_SERVICE${unit.set}/${unit.number}.png"
            Picasso.with(imgUnit.context)
                    .load(urlUnit)
                    .resize(50,70)
                    .into(imgUnit)
        }
    }

}