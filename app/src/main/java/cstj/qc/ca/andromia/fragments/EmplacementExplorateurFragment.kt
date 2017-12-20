package cstj.qc.ca.andromia.fragments

import android.R.attr.*
import android.app.Fragment
import android.content.Context
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
import android.view.Display
import android.view.WindowManager
import android.graphics.Bitmap
import cstj.qc.ca.andromia.R.id.contentFrame


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emplacement_explorateur, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emplacement_explorateur_btn_explorer.setOnClickListener {
            mListener!!.onScanClick()
        }

        emplacement_explorateur_btn_affficher_runes.setOnClickListener {
            mListener!!.onShowMyRunesClick()
        }
        updateLocation()

    }

    fun updateLocation(){
        val emplacement = "yartar"
        if(!emplacement.isBlank()){
            when (emplacement){
                "mordukin" ->{
                    drawCurrentLocation(95,90)
                }
                "yartar"->{
                    drawCurrentLocation(285,50)
                }
                "everlund"->{
                    drawCurrentLocation(195,240)
                }
                "atomico"->{
                    drawCurrentLocation(405,167)
                }
                "bézantur"->{
                    drawCurrentLocation(737,66)
                }
                "trois-épées"->{
                    drawCurrentLocation(340,440)
                }
                "inoxis"->{
                    drawCurrentLocation(557,395)
                }
                "lunargent"->{
                    drawCurrentLocation(260,540)
                }
                "star nation"->{
                    drawCurrentLocation(230,375)
                }
                "indigo"->{
                    drawCurrentLocation(40,310)
                }
                "ilm garde"->{
                    drawCurrentLocation(265,705)
                }
                "cordisphère"->{
                    drawCurrentLocation(630,535)
                }
                "bois d'elm"->{
                    drawCurrentLocation(760,480)
                }
                "myth dranon"->{
                    drawCurrentLocation(768,200)
                }
                "apollo"->{
                    drawCurrentLocation(915,190)
                }
                "rafalo land"->{
                    drawCurrentLocation(985,100)
                }
                "deux-étoiles"->{
                    drawCurrentLocation(985,340)
                }
                "bourok torn"->{
                    drawCurrentLocation(725,762)
                }
                "volcano"->{
                    drawCurrentLocation(850,750)
                }
                "n'jast"->{
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
        @JvmStatic
        fun newInstance() = EmplacementExplorateurFragment()
    }
}
