package cstj.qc.ca.andromia.fragments

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cstj.qc.ca.andromia.R
import kotlinx.android.synthetic.main.fragment_emplacement_explorateur.*
import android.content.res.Resources
import android.graphics.*
import android.graphics.Bitmap


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

        emplacement_explorateur_btn_afficher_runes.setOnClickListener {
            mListener!!.onShowMyRunesClick()
        }

        drawMap(405,167)
    }

    fun drawMap(xPost:Int,yPos:Int){
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        var bitmap = Bitmap.createBitmap(width, height/2,Bitmap.Config.ARGB_8888)
        var cnv = Canvas(bitmap)
        var paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.alpha = 123
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
