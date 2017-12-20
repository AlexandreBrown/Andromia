package cstj.qc.ca.andromia.fragments

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cstj.qc.ca.andromia.R
import kotlinx.android.synthetic.main.fragment_emplacement_explorateur.*
import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap
import android.widget.TextView
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import cstj.qc.ca.andromia.R.id.contentFrame
import cstj.qc.ca.andromia.activities.ConnexionActivity
import cstj.qc.ca.andromia.dialogs.RunesDialog
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_emplacement_explorateur.view.*
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EmplacementExplorateurFragment.OnEmplacementExplorateurInteractionListener] interface
 * to handle interaction events.
 * Use the [EmplacementExplorateurFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EmplacementExplorateurFragment : Fragment() {
    private var mListener: OnEmplacementExplorateurInteractionListener? = null
    private lateinit var mHrefExplorateur: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emplacement_explorateur, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            mHrefExplorateur = it.getString(EmplacementExplorateurFragment.ARG_HREF)
        }

        emplacement_explorateur_btn_explorer.setOnClickListener {
            mListener!!.onScanClick()
        }

        emplacement_explorateur_btn_afficher_runes.setOnClickListener {
            mListener!!.onShowMyRunesClick()
        }

        val prefs = activity.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val token:String = prefs.getString(EXPLORATEUR_KEY, "")
        if(token.isNotEmpty() && !mHrefExplorateur!!.isBlank()){
            var request = (BASE_URL +"explorateurs/$mHrefExplorateur").httpGet().header("Authorization" to "Bearer $token")
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        val explorateur = Explorateur(result.get())
                        //updateLocation(explorateur.location)
                        view!!.emplacement_explorateur!!.text = explorateur.location
                    }
                    else -> {
                        logout()
                    }
                }
            }
        }else{
            logout()
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

<<<<<<< HEAD
    fun updateLocation(location:String){
        if(!location.isBlank() && location !== "Nulle part"){
=======
    private fun updateLocation(location:String){
        if(!location.isBlank()){
>>>>>>> 1fe8b9428a84c7c15215f529b5c7e74566d3de51
            when (location){
                "Mordukin" ->{
                    drawCurrentLocation(95,90)
                }
                "Yartar"->{
                    drawCurrentLocation(285,50)
                }
                "Everlund"->{
                    drawCurrentLocation(195,240)
                }
                "Atomico"->{
                    drawCurrentLocation(405,167)
                }
                "Bézantur"->{
                    drawCurrentLocation(737,66)
                }
                "Trois-Épées"->{
                    drawCurrentLocation(340,440)
                }
                "Inoxis"->{
                    drawCurrentLocation(557,395)
                }
                "Lunargent"->{
                    drawCurrentLocation(260,540)
                }
                "Star Nation"->{
                    drawCurrentLocation(230,375)
                }
                "Indigo"->{
                    drawCurrentLocation(40,310)
                }
                "Ilm Garde"->{
                    drawCurrentLocation(265,705)
                }
                "Cordisphère"->{
                    drawCurrentLocation(630,535)
                }
                "Bois d'Elm"->{
                    drawCurrentLocation(760,480)
                }
                "Myth Dranor"->{
                    drawCurrentLocation(768,200)
                }
                "Apollo"->{
                    drawCurrentLocation(915,190)
                }
                "Rafalo Land"->{
                    drawCurrentLocation(985,100)
                }
                "Deux-Étoiles"->{
                    drawCurrentLocation(985,340)
                }
                "Bourok Torn"->{
                    drawCurrentLocation(725,762)
                }
                "Volcano"->{
                    drawCurrentLocation(850,750)
                }
                "N'Jast"->{
                    drawCurrentLocation(970,605)
                }
            }
        }
    }

    fun drawCurrentLocation(xPost:Int,yPos:Int){
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        var bitmap = Bitmap.createBitmap(width, height/2,Bitmap.Config.ARGB_8888)
        var cnv = Canvas(bitmap)
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.alpha = 200
        val radius = 8
        val density = resources.displayMetrics.density
        cnv.drawCircle(
                (xPost.toFloat()),
                (yPos.toFloat()),
                radius*density,
                paint
        )
        emplacement_explorateur_point.setImageBitmap(bitmap)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEmplacementExplorateurInteractionListener) {
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
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnEmplacementExplorateurInteractionListener {
        fun onShowMyRunesClick()
        fun onScanClick()
    }

    companion object {
        private var ARG_HREF :String? = null
        @JvmStatic
        fun newInstance(href:String) :EmplacementExplorateurFragment=
                EmplacementExplorateurFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_HREF,href)
                    }
                }
    }

}
