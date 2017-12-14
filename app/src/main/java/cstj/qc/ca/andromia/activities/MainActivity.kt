package cstj.qc.ca.andromia.activities

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.fragments.OnListItemFragmentInteractionListener
import cstj.qc.ca.andromia.fragments.ScannerPortalFragment
import cstj.qc.ca.andromia.fragments.UnitsExplorateurFragment
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Item
import cstj.qc.ca.andromia.models.Unit
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
                                        , ScannerPortalFragment.ScanResultReceiver
                                        , OnListItemFragmentInteractionListener {
    override fun scanResultData(codeContent: String) {
        Toast.makeText(this,"Code content : $codeContent",Toast.LENGTH_LONG).show()
    }

    override fun scanResultData(noScanData:ScannerPortalFragment.NoScanResultException) {
        Toast.makeText(this,noScanData.message,Toast.LENGTH_LONG).show()
    }

    override fun onListItemFragmentInteraction(item: Item?) {
        Runnable {
            val transaction = fragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

            when(item){
                is Unit -> {
                    //transaction.replace(R.id.contentFrame, UnitDetail.newInstance(item.href))
                    //transaction.addToBackStack("DetailUnit${item.href}")
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

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

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
                val intent = Intent(this,ConnexionActivity::class.java)

                // Suppression du token
                val myPrefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                val keyRetrieved = myPrefs.getString(EXPLORATEUR_KEY,"")
                Toast.makeText(this,"Explorateur avant suppression: $keyRetrieved", Toast.LENGTH_SHORT).show()
                myPrefs.edit().remove(EXPLORATEUR_KEY).apply()

                val after = myPrefs.getString(EXPLORATEUR_KEY,"")
                Toast.makeText(this,"Explorateur présent après suppression: ${ if(after.isNotEmpty())after else "Aucun explorateur trouvé"}", Toast.LENGTH_LONG ).show()

                // Retour à l'écran de connexion
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

                this.finish()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_unit -> {
                val transaction = fragmentManager.beginTransaction()

                transaction.replace(R.id.contentFrame, UnitsExplorateurFragment.newInstance(1))
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
