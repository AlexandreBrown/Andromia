package cstj.qc.ca.andromia.adapters

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Color.BLUE
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.content.res.AppCompatResources.getDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.R.drawable.*
import cstj.qc.ca.andromia.activities.MainActivity
import cstj.qc.ca.andromia.dialogs.RunesDialog



import cstj.qc.ca.andromia.models.Rune

import kotlinx.android.synthetic.main.fragment_rune.view.*




class RuneRecyclerViewAdapter(
        private val mValues: List<Rune>,
        private val mListener: RunesDialog.OnRunesDialogListener?)
    : RecyclerView.Adapter<RuneRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_rune, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mValues[position])
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var rune:Rune? = null
        fun bind(unRune:Rune){
            this.rune = unRune

            //Binding des valeurs à la vue
            mView.rune_name.text = rune!!.nom
            mView.rune_qty.text = rune!!.quantite.toString()
            when(rune!!.nom){
                "air"->{
                    mView.img_rune.setBackgroundResource(air)
                }
                "darkness"->{
                    mView.img_rune.setBackgroundResource(darkness)
                }
                "earth"->{
                    mView.img_rune.setBackgroundResource(earth)
                }
                "energy"->{
                    mView.img_rune.setBackgroundResource(energy)
                }
                "fire"->{
                    mView.img_rune.setBackgroundResource(fire)
                }
                "life"->{
                    mView.img_rune.setBackgroundResource(life)
                }
                "light"->{
                    mView.img_rune.setBackgroundResource(light)
                }
                "logic"->{
                    mView.img_rune.setBackgroundResource(logic)
                }
                "music"->{
                    mView.img_rune.setBackgroundResource(music)
                }
                "space"->{
                    mView.img_rune.setBackgroundResource(space)
                }
                "toxic"->{
                    mView.img_rune.setBackgroundResource(toxic)
                }
                "water"->{
                    mView.img_rune.setBackgroundResource(water)
                }
            }
        }
    }
}
