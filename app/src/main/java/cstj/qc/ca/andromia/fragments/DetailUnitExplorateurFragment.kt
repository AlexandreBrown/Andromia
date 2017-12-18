package cstj.qc.ca.andromia.fragments

import android.app.Fragment
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.squareup.picasso.Picasso
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.models.Unit
import kotlinx.android.synthetic.main.card_unit.*
import kotlinx.android.synthetic.main.fragment_unit_detail.*
import org.json.JSONArray

/**
 * Created by Guillaume on 2017-12-17.
 */
class DetailUnitExplorateurFragment: Fragment() {

    private lateinit var token:String
    private lateinit var href:String

    override fun onCreate(savedInstanceState: Bundle?) {

        token = arguments!!.getString(ARG_TOKEN)
        href = arguments!!.getString(ARG_HREF)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(token.isNotEmpty() && !href!!.isBlank()){
            var request = ("$href").httpGet().header("Authorization" to "Bearer $token")
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        val unit = Unit(result.get())

                        lblNomUnit.text = unit.name
                        lblLife.text = unit.life
                        lblSpeed.text = unit.speed
                        lblSet.text = unit.set
                        lblNumber.text = unit.number

                        Picasso.with(imgDetailUnit.context)
                                .load(unit.imageURL)
                                .into(imgDetailUnit)

                        if (!unit.affinity.isBlank()){
                            afficherRuneAffinity(unit.affinity)
                        }

                        if (unit.abilities.length() > 0){
                            afficherRunesAbilities(unit.abilities)
                        }

                        if (unit.weapons.length() > 0){
                            afficherRunesWeapons(unit.weapons)
                        }

                        if (!unit.ultimate.isBlank()){
                            afficherRuneUltimate(unit.ultimate)
                        }
                    }
                }
            }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unit_detail, container, false)
    }

    companion object {
        private val ARG_TOKEN = "token"
        private val ARG_HREF = "href"

        fun newInstance(token: String, mHrefUnit: String? ) : DetailUnitExplorateurFragment {
            val fragment = DetailUnitExplorateurFragment()
            val args = Bundle()

            args.putString(ARG_TOKEN, token)
            args.putString(ARG_HREF, mHrefUnit)

            fragment.arguments = args
            return fragment
        }
    }


    private fun afficherRuneAffinity(typeRune: String){
        val type = getRuneType(typeRune)

        imgAffinity.setBackgroundResource(type)
    }

    private fun afficherRunesAbilities(lstAbilities: JSONArray){
        for (i in 0..(lstAbilities.length()-1)){
            val type = getRuneType(lstAbilities[i].toString())

            when(i){
                0 -> {
                    imgAbility1.setBackgroundResource(type)
                }
                1 -> {
                    imgAbility2.setBackgroundResource(type)
                }
                2 -> {
                    imgAbility3.setBackgroundResource(type)
                }
                3 -> {
                    imgAbility4.setBackgroundResource(type)
                }
            }
        }
    }

    private fun afficherRunesWeapons(lstWeapons: JSONArray){
        for (i in 0..(lstWeapons.length()-1)){
            val type = getRuneType(lstWeapons[i].toString())

            when(i){
                0 -> {
                    imgWeapon1.setBackgroundResource(type)
                }
                1 -> {
                    imgWeapon2.setBackgroundResource(type)
                }
            }
        }
    }

    private fun afficherRuneUltimate(typeRune: String){
        val type = getRuneType(typeRune)

        imgUltimate.setBackgroundResource(type)
    }

    private fun getRuneType(type:String): Int{

        val ressourceId = resources.getIdentifier(type, "drawable", context.packageName)

        return ressourceId

    }



}