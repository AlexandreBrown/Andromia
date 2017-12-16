package cstj.qc.ca.andromia.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.helpers.ANDROMIA_CONNEXION_SERVICE
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*
import org.json.JSONObject

class ConnexionActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

    }

    fun createMutableListExplorateurs(json:Json): MutableList<Explorateur> {
        val tabJson = json.array()
        val lstExplo = mutableListOf<Explorateur>()
        (0..(tabJson.length() -1)).mapTo(lstExplo) { Explorateur(Json(tabJson[it].toString())) }
        return lstExplo
    }

    fun onLoginClick(view:View){
        if(view.id == R.id.login_btn_signin){
            if(Validator.emailValidator(login_et_email.text.toString())){
                if(login_et_password.length() > 0 ){
                    var explorateurJSON = JSONObject()
                    explorateurJSON.put("courriel",login_et_email.text.toString())
                    explorateurJSON.put("motDePasse",login_et_password.text.toString())
                    Fuel.post(ANDROMIA_CONNEXION_SERVICE).body(explorateurJSON.toString()).responseJson { _, response, result ->
                        when {
                            response.httpStatusCode == 200 -> {
                                var r = response.data
                                val key = "" // Need to retrieve API KEY from server
                                val prefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                                prefs.edit().putString(EXPLORATEUR_KEY,key).apply()
                                val keyRetrieved = prefs.getString(EXPLORATEUR_KEY,"")

                                Toast.makeText(this,"Explorateur : $keyRetrieved", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this,MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                                this.finish()

                            }
                            response.httpStatusCode in 400..499 -> {
                                login_et_password.text.clear()
                                Toast.makeText(this,"Votre courriel ou mot de passe est invalide",Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                val mSnackbar = Snackbar.make(mainContainer!!, "Connexion au serveur impossible", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Réessayer") {
                                            onLoginClick(view)
                                        }
                                mSnackbar.show()
                            }
                        }

                    }

                }else{
                    login_et_password.error = "Le mot de passe ne peut pas être vide"
                }
            }else{
                login_et_email.error = "Le format du courriel est invalide"
            }
        }
    }

    fun onCreateAccountClick(view: View){
        if(view.id == R.id.login_btn_signup){
            val intent = Intent(this,CreationCompteActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        }
    }


}
