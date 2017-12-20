package cstj.qc.ca.andromia.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.view.LayoutInflater
import android.widget.RelativeLayout

import com.squareup.picasso.Picasso
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.fragments.OnListItemFragmentInteractionListener

import cstj.qc.ca.andromia.models.Item
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.text.SimpleDateFormat

/**
 * Created by Guillaume on 2017-12-12.
 */
class RecyclerViewAdapter(private val mValues:List<Item>, private val mListener: OnListItemFragmentInteractionListener?): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent,false )
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
        var lblNameUnit = mView.lblChamp1List
        var lblNumberUnit = mView.lblChamp2List
        var lblSetUnit = mView.lblChamp3List
        var imgUnit = mView.imgUnitList
        var item: Item? = null

        fun bind(item: Item){
            val info = item.getAffichage()

            // Si c'est un Unit
            if(info.size == 4){
                //this.lblSetUnit.text = info[2]
                Picasso.with(imgUnit.context)
                        .load(info[3])
                        .into(imgUnit)
            }else {
                //this.lblSetUnit.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(info[2])
                  //      info[2]
                modifierCard()
            }
            this.lblNameUnit.text = info[0]
            this.lblNumberUnit.text = info[1]
            this.lblSetUnit.text = info[2]
            this.item = item

        }

        private fun modifierCard(){

            // Affichage premier champ
            mView.lblTitreChamp1.text = R.string.emplacementDepart.toString()
            mView.lblTitreChamp1.setPadding(10,0,0,0)
            mView.lblChamp1List.setPadding(10,0,0,0)

            //Affichage deuxième champ
            mView.lblTitreChamp2.text = R.string.emplacementArrivee.toString()
            mView.lblTitreChamp2.setPadding(10,0,0,0)
            mView.lblChamp2List.setPadding(10,0,0,0)

            // Affichage troisième champ
            mView.lblTitreChamp3.text = R.string.dateExploration.toString()
            mView.lblTitreChamp3.setPadding(10,0,0,0)
            mView.lblChamp3List.setPadding(10,0,0,0)

            // Retirer l'image
            mView.imgUnitList.visibility = View.GONE
        }
    }

}