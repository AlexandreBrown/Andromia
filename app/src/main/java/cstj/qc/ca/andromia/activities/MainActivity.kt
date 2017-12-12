package cstj.qc.ca.andromia.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.fragments.ScannerPortalFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
                                        , ScannerPortalFragment.OnFragmentInteractionListener{

    private val REQUEST_CAMERA : Int = 1

    override fun onRequestPermissionResult(requestCode: Int, permission: Array<String>, grantResults: Array<Int>) {
        fun onRequestPermissionResult(requestCode:Int,permission:Array<String>,grantResults:Array<Int>){
            when(requestCode){
                REQUEST_CAMERA -> if(grantResults.isNotEmpty()){
                    val cameraAccepted:Boolean = (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if(cameraAccepted){
                        Toast.makeText(this,"Permission allouée!", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,"Permission non-allouée!", Toast.LENGTH_LONG).show()
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                                displayAlertMessage("Vous devez autoriser l'accès aux deux permissions",
                                        DialogInterface.OnClickListener { _, i ->
                                            requestPermissions(Array<String>(android.support.design.R.id.auto){ Manifest.permission.CAMERA },REQUEST_CAMERA)
                                        })
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    fun displayAlertMessage(message:String,listener: DialogInterface.OnClickListener){
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK",listener)
                .setNegativeButton("Annuler",null)
                .create()
                .show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame,ScannerPortalFragment.newInstance())
        transaction.commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_logout -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        fun newInstance() : MainActivity{
            return MainActivity()
        }
    }

}
