package cstj.qc.ca.andromia.fragments

import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson

import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.adapters.RecyclerViewAdapter
import cstj.qc.ca.andromia.helpers.ANDROMIA_EXPLORATEUR_SERVICES
import cstj.qc.ca.andromia.helpers.SERVEUR_ANDROMIA_SERVICE
import cstj.qc.ca.andromia.models.Unit
import org.json.JSONObject


/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class UnitsExplorateurFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: OnListItemFragmentInteractionListener? = null
    private var units = mutableListOf<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_unit_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }

            view.adapter = RecyclerViewAdapter(units, mListener)

            val urlUnitsExplorateur = ANDROMIA_EXPLORATEUR_SERVICES + "/" + "uuidToken" + "/units"

            /*urlUnitsExplorateur.httpGet().responseJson {request, response, result ->
                createUnitList(result.get())
                view.adapter.notifyDataSetChanged()
            }*/

        }
        return view
    }

    fun createUnitList(json: Json){
        units.clear()
        val tabJson = json.array()

        for (i in 0.. (tabJson.length() - 1 )){
            units.add(Unit(Json(tabJson[i].toString())))
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

        private val ARG_COLUMN_COUNT = "column-count"

        fun newInstance(columnCount: Int): UnitsExplorateurFragment {
            val fragment = UnitsExplorateurFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
