package cstj.qc.ca.andromia.fragments

import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet

import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.activities.ConnexionActivity
import cstj.qc.ca.andromia.adapters.RecyclerViewAdapter
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Exploration


class ExplorationsExplorateurFragment : Fragment() {
    private var mColumnCount = 1
    private lateinit var token:String
    private lateinit var href:String
    private var mListener: OnListItemFragmentInteractionListener? = null
    private var explorations = mutableListOf<Exploration>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            token = arguments.getString(ARG_TOKEN)
            href = arguments.getString(ARG_HREF)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            view.adapter = RecyclerViewAdapter(explorations, mListener)

            // On vérifie si l'utilisateur est connecté et si il c'est bien un compte existant
            if(token.isNotEmpty() && !href!!.isBlank()){
                var request = (BASE_URL+"explorateurs/$href/explorations").httpGet()
                request.httpHeaders["Authorization"] = "Bearer $token"
                request.responseJson{ _, response, result ->
                    when{
                        (response.httpStatusCode == 200) ->{
                            createExplorationList(result.get())
                            view.adapter.notifyDataSetChanged()
                        }else -> {

                        }
                    }
                }
            }else {
                logout()
            }

        }
        return view
    }

    private fun createExplorationList(json: Json){
        explorations.clear()

        val tabJson = json.array()
        for (i in 0.. (tabJson.length() - 1 )){
            explorations.add(Exploration(Json(tabJson[i].toString())))
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListItemFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    companion object {

        private val ARG_TOKEN = "token"
        private val ARG_HREF = "href"

        fun newInstance(token: String, mHrefExplorateur: String?): ExplorationsExplorateurFragment {
            val fragment = ExplorationsExplorateurFragment()
            val args = Bundle()
            args.putString(ARG_TOKEN, token)
            args.putString(ARG_HREF, mHrefExplorateur)
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
}
