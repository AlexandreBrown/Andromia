package cstj.qc.ca.andromia.fragments

import android.Manifest
import android.Manifest.permission.CAMERA
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.R.id.auto
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.checkPermission
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.Result
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.activities.MainActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScannerPortalFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScannerPortalFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ScannerPortalFragment : Fragment(),ZXingScannerView.ResultHandler {
    private var listener: OnFragmentInteractionListener? = null
    private var scannerView:ZXingScannerView? = null
    private val REQUEST_CAMERA : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scannerView = ZXingScannerView(this.context)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(this.context,"Permission allouée!",Toast.LENGTH_LONG).show()
            }else{
                requestPermissions()
            }
        }
    }

    private  fun checkPermission(): Boolean = (ContextCompat.checkSelfPermission(this.context as Context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

    private fun requestPermissions(){
        var strinArray:Array<String> = arrayOf(CAMERA)
        strinArray[0] = CAMERA
        ActivityCompat.requestPermissions(this.activity,strinArray,REQUEST_CAMERA)
    }

    override fun onResume() {
        super.onResume()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = ZXingScannerView(this.context)
                }
                scannerView!!.setResultHandler(this)
            }else{
                requestPermissions()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView!!.stopCamera()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner_portal, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun handleResult(p0: Result?) {
        var scanResult:String = p0!!.text
        var builder:AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Résultat de l'exploration")
                .setPositiveButton("OK", { _, i ->
                    scannerView!!.resumeCameraPreview(this)
                })
        builder.setNeutralButton("Afficher", { _, i ->
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(scanResult))
            startActivity(intent)
        })
        builder.setMessage(scanResult)
        var alert:AlertDialog = builder.create()
        alert.show()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onRequestPermissionResult(requestCode:Int,permission:Array<String>,grantResults:Array<Int>)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment ScannerPortalFragment.
         */
        @JvmStatic
        fun newInstance():Fragment = ScannerPortalFragment()
    }
}
