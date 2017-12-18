package cstj.qc.ca.andromia.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.fragments.DetailUnitExplorateurFragment
import cstj.qc.ca.andromia.fragments.OnListItemFragmentInteractionListener
import cstj.qc.ca.andromia.fragments.ScannerPortalFragment
import cstj.qc.ca.andromia.fragments.UnitsExplorateurFragment
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import cstj.qc.ca.andromia.models.Item
import cstj.qc.ca.andromia.models.Unit
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
                                        , ScannerPortalFragment.ScanResultReceiver
                                        , OnListItemFragmentInteractionListener {

    private var mHrefExplorateur:String? = null


    override fun scanResultData(codeContent: String) {
        Toast.makeText(this,"Code content : $codeContent",Toast.LENGTH_LONG).show()
    }

    override fun scanResultData(noScanData:ScannerPortalFragment.NoScanResultException) {
        Toast.makeText(this,noScanData.message,Toast.LENGTH_LONG).show()
    }

    override fun onListItemFragmentInteraction(item: Item?) {
        val prefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val token:String = prefs.getString(EXPLORATEUR_KEY, "")

        Runnable {
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

            when(item){
                is Unit -> {
                    transaction.replace(R.id.contentFrame, DetailUnitExplorateurFragment.newInstance(token, item.href))
                    transaction.addToBackStack("DetailUnit${item.href}")
                }
            }
            transaction.commit()
        }.run()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        if(intent.getStringExtra("hrefExplorateur") != null){
            mHrefExplorateur = intent.getStringExtra("hrefExplorateur").toString()
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val prefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val token:String = prefs.getString(EXPLORATEUR_KEY, "")
        if(token.isNotEmpty() && !mHrefExplorateur!!.isBlank()){
            var request = (BASE_URL +"explorateurs/$mHrefExplorateur").httpGet().header("Authorization" to "Bearer $token")
            request.responseJson{ request, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        val explorateur = Explorateur(result.get())

                        nav_email.text = explorateur.courriel
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


    fun onScanClick(view:View){
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrame, ScannerPortalFragment.newInstance())
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
        return when (item.itemId) {
            R.id.action_logout ->
            {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout(){
        val intent = Intent(this,ConnexionActivity::class.java)

        // Suppression du token
        val myPrefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        myPrefs.edit().remove(EXPLORATEUR_KEY).apply()

        // Retour à l'écran de connexion
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        this.finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        val prefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val token = prefs.getString(EXPLORATEUR_KEY, "")

        when (item.itemId) {
            R.id.nav_unit -> {
                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.contentFrame, UnitsExplorateurFragment.newInstance(token, mHrefExplorateur))
                transaction.commit()
            }
            R.id.nav_exploration -> {
                //val transaction = fragmentManager.beginTransaction()
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
