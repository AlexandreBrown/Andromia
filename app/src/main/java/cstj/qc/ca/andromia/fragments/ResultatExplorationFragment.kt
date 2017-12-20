package cstj.qc.ca.andromia.fragments

import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet

import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.activities.ConnexionActivity
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.helpers.SERVEUR_ANDROMIA_SERVICE
import cstj.qc.ca.andromia.models.AndromiaExploration
import cstj.qc.ca.andromia.models.Explorateur
import cstj.qc.ca.andromia.models.Rune
import kotlinx.android.synthetic.main.fragment_exploration_resultat.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ResultatExplorationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ResultatExplorationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultatExplorationFragment : Fragment() {

    private lateinit var token:String
    private lateinit var href:String
    private lateinit var codeExploration:String
    private var exploration:JSONObject = JSONObject()
    private var location:String = ""
    private var runesExplorateur = mutableListOf<Rune>()


    private var mListener: OnResultatExplorationFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            token = arguments.getString(ARG_TOKEN)
            href = arguments.getString(ARG_HREF)
            codeExploration = arguments.getString(ARG_EXPLORATION)
        }
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exploration_resultat_btn_Capturer.setOnClickListener {
            mListener!!.onCapturerUnitClick(exploration)
        }

        exploration_resultat_btn_affficher_runes.setOnClickListener {
            mListener!!.onShowMyRunesClick()
        }

        exploration_resultat_btn_Terminer.setOnClickListener {
            var unitVide = JSONObject()
            exploration.put("unit", unitVide)
            mListener!!.onTerminerExplorationClick(exploration)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if(token.isNotEmpty() && !href!!.isBlank()){
            var request = (BASE_URL+"explorateurs/"+href).httpGet()
            request.httpHeaders["Authorization"] = "Bearer $token"
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        var explorateur = Explorateur(result.get())

                        location = explorateur.location
                        runesExplorateur = explorateur.lstRunes
                    }else -> {
                    location = ""
                    }
                }
            }

            val url = SERVEUR_ANDROMIA_SERVICE +"64FB7B69-20D1-4353-83A1-B2FC7EF07276"
            //var request = (SERVEUR_ANDROMIA_SERVICE + codeExploration).httpGet()
            url.httpGet().responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        var resultat = AndromiaExploration(result.get())

                        exploration.put("dateExploration", resultat.dateExploration )
                        exploration.put("locationDepart", location)
                        exploration.put("locationDestination", resultat.destination)
                        exploration.put("runes", resultat.runes)
                        exploration.put("unit",resultat.unit)


                        if (resultat.runes.length() > 0 ){
                            AfficherRunesAcquise(resultat.runes)
                        }else{
                            runesAcquises.visibility = View.GONE
                        }

                        if (resultat.unit.length() > 0 ){
                            AfficherUnitResultatExploration(resultat.unit)
                            var estValide = false

                            if (runesExplorateur[0].quantite >= resultat.unit.getJSONObject("kernel").getInt("air")
                                    && runesExplorateur[1].quantite >= resultat.unit.getJSONObject("kernel").getInt("darkness")
                                    && runesExplorateur[2].quantite >= resultat.unit.getJSONObject("kernel").getInt("earth")
                                    && runesExplorateur[3].quantite >= resultat.unit.getJSONObject("kernel").getInt("energy")
                                    && runesExplorateur[4].quantite >= resultat.unit.getJSONObject("kernel").getInt("fire")
                                    && runesExplorateur[5].quantite >= resultat.unit.getJSONObject("kernel").getInt("life")
                                    && runesExplorateur[6].quantite >= resultat.unit.getJSONObject("kernel").getInt("light")
                                    && runesExplorateur[7].quantite >= resultat.unit.getJSONObject("kernel").getInt("logic")
                                    && runesExplorateur[8].quantite >= resultat.unit.getJSONObject("kernel").getInt("music")
                                    && runesExplorateur[9].quantite >= resultat.unit.getJSONObject("kernel").getInt("space")
                                    && runesExplorateur[10].quantite >= resultat.unit.getJSONObject("kernel").getInt("toxic")
                                    && runesExplorateur[11].quantite >= resultat.unit.getJSONObject("kernel").getInt("water")) {
                                estValide = true
                            }
                            exploration_resultat_btn_Capturer.isEnabled = estValide
                        } else {
                            UnitResultatExploration.visibility = View.GONE
                            exploration_resultat_btn_Capturer.isEnabled = false
                        }
                    }
                }
            }


        }
        else{
            logout()
        }

            /*// On vérifie si l'utilisateur est connecté et si il c'est bien un compte existant
            if(token.isNotEmpty() && !href!!.isBlank()){
                val url = SERVEUR_ANDROMIA_SERVICE +"64FB7B69-20D1-4353-83A1-B2FC7EF07276"
                //var request = (SERVEUR_ANDROMIA_SERVICE + codeExploration).httpGet()
                url.httpGet().responseJson{ _, response, result ->
                    when{
                        (response.httpStatusCode == 200) ->{
                            var resultat = AndromiaExploration(result.get())

                            exploration.put("dateExploration", resultat.dateExploration )
                            exploration.put("locationDepart", location)
                            exploration.put("locationDestination", resultat.destination)
                            exploration.put("runes", resultat.runes)
                            exploration.put("unit",resultat.unit)
                            if (resultat.runes.length() > 0 ){
                                AfficherRunesAcquise(resultat.runes)
                            }else{
                                runesAcquises.visibility = View.GONE
                            }

                            if (resultat.unit.length() > 0 ){
                                AfficherUnitResultatExploration(resultat.unit)
                                var estValide = calculerNombreRunes(resultat.unit)
                                //exploration_resultat_btn_Capturer.isEnabled = calculerNombreRunes(resultat.unit)
                                exploration_resultat_btn_Capturer.isEnabled = estValide
                            } else {
                                UnitResultatExploration.visibility = View.GONE
                                exploration_resultat_btn_Capturer.isEnabled = false
                            }
                        }
                    }
                }
            }else {
                logout()
            }*/


        return inflater.inflate(R.layout.fragment_exploration_resultat, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnResultatExplorationFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnResultatExplorationFragmentListener {
        fun onCapturerUnitClick(jsonObject: JSONObject)
        fun onShowMyRunesClick()
        fun onTerminerExplorationClick(jsonObject: JSONObject)
    }

    companion object {
        private val ARG_TOKEN = "token"
        private val ARG_HREF = "href"
        private val ARG_EXPLORATION = "codeExploration"

        fun newInstance(codeExploration: String, href: String?, token: String): ResultatExplorationFragment {

            val fragment = ResultatExplorationFragment()
            val args = Bundle()

            args.putString(ARG_EXPLORATION, codeExploration)
            args.putString(ARG_HREF, href)
            args.putString(ARG_TOKEN, token)

            fragment.arguments = args
            return fragment
        }
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

    private fun getLocationDepart():String{
        var location = "Nulle part"
        if(token.isNotEmpty() && !href!!.isBlank()){
            var request = (BASE_URL+"explorateurs/"+href).httpGet()
            request.httpHeaders["Authorization"] = "Bearer $token"
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        var explorateur = Explorateur(result.get())

                        location = explorateur.location
                    }else -> {
                        location = ""
                    }
                }
            }
            return location
        }
        else{
            logout()
            return location
        }
    }

    private fun calculerNombreRunes(unitResultat: JSONObject): Boolean{
        var estValide = false
        if(token.isNotEmpty() && !href.isBlank()){
            var request = (BASE_URL+"explorateurs/"+href).httpGet().header("Authorization" to "Bearer $token")
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        var explorateur = Explorateur(result.get())

                        if (explorateur.lstRunes[0].quantite >= unitResultat.getJSONObject("kernel").getInt("air")
                                && explorateur.lstRunes[1].quantite >= unitResultat.getJSONObject("kernel").getInt("darkness")
                                && explorateur.lstRunes[2].quantite >= unitResultat.getJSONObject("kernel").getInt("earth")
                                && explorateur.lstRunes[3].quantite >= unitResultat.getJSONObject("kernel").getInt("energy")
                                && explorateur.lstRunes[4].quantite >= unitResultat.getJSONObject("kernel").getInt("fire")
                                && explorateur.lstRunes[5].quantite >= unitResultat.getJSONObject("kernel").getInt("life")
                                && explorateur.lstRunes[6].quantite >= unitResultat.getJSONObject("kernel").getInt("light")
                                && explorateur.lstRunes[7].quantite >= unitResultat.getJSONObject("kernel").getInt("logic")
                                && explorateur.lstRunes[8].quantite >= unitResultat.getJSONObject("kernel").getInt("music")
                                && explorateur.lstRunes[9].quantite >= unitResultat.getJSONObject("kernel").getInt("space")
                                && explorateur.lstRunes[10].quantite >= unitResultat.getJSONObject("kernel").getInt("toxic")
                                && explorateur.lstRunes[11].quantite >= unitResultat.getJSONObject("kernel").getInt("water"))
                        {
                            estValide = true
                        }
                    }
                }
            }
            return estValide
        }
        else{
            logout()
            return estValide
        }

    }

    private fun AfficherRunesAcquise(runesAAfficher: JSONObject){

    }

    private fun AfficherUnitResultatExploration(unitAAfficher: JSONObject){
        lbl_resultat_nomUnit.text = unitAAfficher.getString("name")
        lbl_resultat_LifeUnit.text = unitAAfficher.getInt("life").toString()
        lbl_resultat_SpeedUnit.text = unitAAfficher.getInt("speed").toString()
        lbl_resultat_AffinityUnit.text = unitAAfficher.getString("affinity")
    }

}// Required empty public constructor
