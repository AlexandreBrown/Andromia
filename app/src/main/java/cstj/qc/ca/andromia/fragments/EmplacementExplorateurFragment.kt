package cstj.qc.ca.andromia.fragments

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cstj.qc.ca.andromia.R
import kotlinx.android.synthetic.main.fragment_emplacement_explorateur.*
import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap
import android.renderscript.Sampler
import android.view.animation.LinearInterpolator
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import cstj.qc.ca.andromia.activities.ConnexionActivity
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import kotlinx.android.synthetic.main.fragment_emplacement_explorateur.view.*


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

        if (isAdded && activity != null) {
            val prefs = activity.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            val token:String = prefs.getString(EXPLORATEUR_KEY, "")
            if(token.isNotEmpty() && !mHrefExplorateur!!.isBlank()){
                var request = (BASE_URL +"explorateurs/$mHrefExplorateur").httpGet().header("Authorization" to "Bearer $token")
                request.responseJson{ _, response, result ->
                    when{
                        (response.httpStatusCode == 200) ->{
                            val explorateur = Explorateur(result.get())
                            view!!.emplacement_explorateur!!.text = explorateur.location
                            startLocationAnimation(explorateur.location)
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
    }

    private fun startLocationAnimation(location: String) {
        //1
        val valueAnimator = ValueAnimator.ofInt(0,255)

        //2
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            updateLocation(location,value)
        }

        //5
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 750
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE

        //6
        valueAnimator.start()
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

    private fun updateLocation(location:String,alpha:Int){
        if(!location.isBlank() && location !== "Nulle part"){
            when (location.toLowerCase()){
                "mordulkin" ->{
                    drawCurrentLocation(95,90,alpha)
                }
                "yartar"->{
                    drawCurrentLocation(285,50,alpha)
                }
                "everlund"->{
                    drawCurrentLocation(195,240,alpha)
                }
                "atomico"->{
                    drawCurrentLocation(405,167,alpha)
                }
                "bézantur"->{
                    drawCurrentLocation(737,66,alpha)
                }
                "trois-épées"->{
                    drawCurrentLocation(340,440,alpha)
                }
                "inoxis"->{
                    drawCurrentLocation(557,395,alpha)
                }
                "lunargent"->{
                    drawCurrentLocation(260,540,alpha)
                }
                "star nation"->{
                    drawCurrentLocation(230,375,alpha)
                }
                "indigo"->{
                    drawCurrentLocation(40,310,alpha)
                }
                "ilm arde"->{
                    drawCurrentLocation(265,705,alpha)
                }
                "cordisphère"->{
                    drawCurrentLocation(630,535,alpha)
                }
                "bois d'elm"->{
                    drawCurrentLocation(760,480,alpha)
                }
                "myth dranor"->{
                    drawCurrentLocation(768,200,alpha)
                }
                "apollo"->{
                    drawCurrentLocation(915,190,alpha)
                }
                "rafalo land"->{
                    drawCurrentLocation(985,100,alpha)
                }
                "deux-étoile"->{
                    drawCurrentLocation(985,340,alpha)
                }
                "bourok torn"->{
                    drawCurrentLocation(725,762,alpha)
                }
                "volcano"->{
                    drawCurrentLocation(850,750,alpha)
                }
                "njast"->{
                    drawCurrentLocation(970,605,alpha)
                }
            }
        }
    }

    fun drawCurrentLocation(xPost:Int,yPos:Int,alpha:Int){
        if (isAdded && activity != null) {
            val width = Resources.getSystem().displayMetrics.widthPixels
            val height = Resources.getSystem().displayMetrics.heightPixels
            var bitmap = Bitmap.createBitmap(width, height / 2, Bitmap.Config.ARGB_8888)
            var cnv = Canvas(bitmap)
            var paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.RED
            paint.alpha = alpha
            val radius = 8
            val density = resources.displayMetrics.density
            cnv.drawCircle(
                    (xPost.toFloat()),
                    (yPos.toFloat()),
                    radius * density,
                    paint
            )
            if (emplacement_explorateur != null) {
                emplacement_explorateur_point.setImageBitmap(bitmap)
            }
        }
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
        fun newInstance(href:String):EmplacementExplorateurFragment=
                EmplacementExplorateurFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_HREF,href)
                    }
                }
    }

}
