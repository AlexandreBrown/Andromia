package cstj.qc.ca.andromia.fragments

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.squareup.picasso.Picasso
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.activities.ConnexionActivity
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Unit
import kotlinx.android.synthetic.main.fragment_unit_detail.*
import org.json.JSONArray


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

        // On vérifie si l'utilisateur est connecté et si il c'est bien un compte existant
        if(token.isNotEmpty() && !href!!.isBlank()){
            var request = ("$href").httpGet().header("Authorization" to "Bearer $token")
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        val unit = Unit(result.get())

                        lblNomUnitDetail.text = unit.name
                        lblLifeDetail.text = unit.life
                        lblSpeedDetail.text = unit.speed
                        lblSetDetail.text = unit.set
                        lblNumberDetail.text = unit.number

                        Picasso.with(imgDetailUnitDetail.context)
                                .load(unit.imageURL)
                                .into(imgDetailUnitDetail)

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
                    } else -> {
                        logout()
                    }
                }
            }
        }
        else{
            logout()
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
        // On affiche la rune de l'affinity du unit
        val type = getRuneType(typeRune)

        imgAffinityDetail.setBackgroundResource(type)
    }

    private fun afficherRunesAbilities(lstAbilities: JSONArray){
        // On parcourt le tableau des abilities du unit pour y afficher les runes
        for (i in 0..(lstAbilities.length()-1)){
            val type = getRuneType(lstAbilities[i].toString())

            when(i){
                0 -> {
                    imgAbility1Detail.setBackgroundResource(type)
                }
                1 -> {
                    imgAbility2Detail.setBackgroundResource(type)
                }
                2 -> {
                    imgAbility3Detail.setBackgroundResource(type)
                }
                3 -> {
                    imgAbility4Detail.setBackgroundResource(type)
                }
            }
        }
    }

    private fun afficherRunesWeapons(lstWeapons: JSONArray){
        // On parcourt le tableau des weapons du unit afin d'y afficher les runes
        for (i in 0..(lstWeapons.length()-1)){
            val type = getRuneType(lstWeapons[i].toString())

            when(i){
                0 -> {
                    imgWeapon1Detail.setBackgroundResource(type)
                }
                1 -> {
                    imgWeapon2Detail.setBackgroundResource(type)
                }
            }
        }
    }

    private fun afficherRuneUltimate(typeRune: String){
        // On veut afficher l'image de la rune pour le ultimate du unit
        val type = getRuneType(typeRune)

        imgUltimateDetail.setBackgroundResource(type)
    }

    private fun getRuneType(type:String): Int{
        // On va chercher le ID de la ressource qui a le nom du type de rune, puis on la retourne
        val ressourceId = resources.getIdentifier(type, "drawable", context.packageName)

        return ressourceId

    }


    private fun logout(){
        val intent = Intent(this.context, ConnexionActivity::class.java)

        // Suppression du token
        val myPrefs = this.activity!!.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        myPrefs.edit().remove(EXPLORATEUR_KEY).apply()

        // Retour à l'écran de connexion
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        this.activity!!.finish()
    }

}