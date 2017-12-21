package cstj.qc.ca.andromia.fragments

import android.app.Activity.RESULT_OK
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.integration.android.IntentIntegrator
import cstj.qc.ca.andromia.R

class ScannerPortalFragment : Fragment() {
    private var listener: ScanResultReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var integrator = IntentIntegrator.forFragment(this)
        integrator.setPrompt(getString(R.string.scan_exploration))
        integrator.setBeepEnabled(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        //retrieve scan result
        val scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent)
        if (scanningResult != null && scanningResult.contents != null && intent != null && resultCode == RESULT_OK) {
            //Nous avons un résultat
            val codeContent = scanningResult.contents
            // On envoie les données à l'activité
            listener!!.scanResultData(codeContent)

        } else {
            // On envoie une exception
            listener!!.scanResultData(NoScanResultException(getString(R.string.exploration_cancelled)))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner_portal, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ScanResultReceiver) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface ScanResultReceiver {
        fun scanResultData(codeContent:String)
        fun scanResultData(noScanData:NoScanResultException)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ScannerPortalFragment.
         */
        @JvmStatic
        fun newInstance():Fragment = ScannerPortalFragment()
    }

    inner class NoScanResultException : Exception {
        constructor(msg: String) : super(msg)
        constructor(cause: Throwable) : super(cause)
        constructor(msg: String, cause: Throwable) : super(msg, cause)
    }
}
