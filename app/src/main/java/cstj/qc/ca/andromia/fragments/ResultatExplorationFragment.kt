package cstj.qc.ca.andromia.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import cstj.qc.ca.andromia.models.Exploration
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
    //private lateinit var location:String
    private var exploration:JSONObject = JSONObject()
    private lateinit var unitAAjouter:JSONObject


    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            token = arguments.getString(ARG_TOKEN)
            href = arguments.getString(ARG_HREF)
            codeExploration = arguments.getString(ARG_EXPLORATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

            // On vérifie si l'utilisateur est connecté et si il c'est bien un compte existant
            if(token.isNotEmpty() && !href!!.isBlank()){
                val url = SERVEUR_ANDROMIA_SERVICE +"64FB7B69-20D1-4353-83A1-B2FC7EF07276"
                //var request = (SERVEUR_ANDROMIA_SERVICE + codeExploration).httpGet()
                url.httpGet().responseJson{ _, response, result ->
                    when{
                        (response.httpStatusCode == 200) ->{
                            var resultat = AndromiaExploration(result.get())
                            var location = getLocationDepart()
                            if (location.isBlank()){
                                location = "Aucune"
                            }
                            exploration.put("dateExploration", resultat.dateExploration )
                            exploration.put("locationDepart", location)
                            exploration.put("destination", resultat.destination)
                            exploration.put("runes", resultat.runes)


                            if (resultat.unit.length() > 0 && calculerNombreRunes(resultat.unit)){
                                btnCapture.visibility = View.VISIBLE
                                btnCapture.isEnabled = true

                            } else {
                                btnCapture.isEnabled = false
                            }
                        }
                    }
                }
            }else {
                logout()
            }


        return inflater.inflate(R.layout.fragment_exploration_resultat, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
    }*/

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
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private val ARG_TOKEN = "token"
        private val ARG_HREF = "href"
        private val ARG_EXPLORATION = "codeExploration"

        // TODO: Rename and change types and number of parameters
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
        var location = "Aucune"
        if(token.isNotEmpty() && !href!!.isBlank()){
            var request = (BASE_URL+href).httpGet().header("Authorization" to "Bearer $token")
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        var explorateur = Explorateur(result.get())

                        location = explorateur.location
                    }
                }
            }
        }
        else{
            logout()
        }
        return location
    }

    private fun calculerNombreRunes(unitResultat: JSONObject): Boolean{
        var estValide = false
        if(token.isNotEmpty() && !href!!.isBlank()){
            var request = (BASE_URL+href).httpGet().header("Authorization" to "Bearer $token")
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
        }
        else{
            logout()
        }
        return estValide
    }

}// Required empty public constructor
