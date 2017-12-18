package cstj.qc.ca.andromia.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.jsonDeserializer
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*
import org.json.JSONArray
import org.json.JSONObject
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import cstj.qc.ca.andromia.helpers.BASE_URL


class ConnexionActivity : AppCompatActivity(){

    private var mSnackbar:Snackbar? = null

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
                    explorateurJSON.put("username",login_et_email.text.toString())
                    explorateurJSON.put("password",login_et_password.text.toString())

                    val req_connexion = (BASE_URL+"connexion").httpPost()
                    req_connexion.httpHeaders["Content-Type"] = "application/json"
                    req_connexion.httpBody = explorateurJSON.toString().toByteArray()
                    req_connexion.responseJson{ _, response, result ->
                        when{ // Si le courriel et mot de passe est bon
                            (response.httpStatusCode == 200) ->{
                                createToken(result.get().obj().getString("token"))
                                // On change d'activity
                                val intent = Intent(this,MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.putExtra("hrefExplorateur",result.get().obj().getJSONObject("user").getString("href").removePrefix(BASE_URL+"explorateurs/"))
                                startActivity(intent)
                                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                                hideKeyboard()
                                this.finish()
                            }
                            (response.httpStatusCode in 400..499) -> {
                                if(mSnackbar != null){
                                    mSnackbar!!.dismiss()
                                }
                                login_et_password.text.clear()
                                hideKeyboard()
                                Toast.makeText(this,"Votre courriel ou mot de passe est invalide",Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                handleNoConnectionError(view)
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

    fun createToken(token:String){
        // On récupère les SharedPreferences
        val prefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        // On ajoute le token dans les SharedPreferences
        prefs.edit().putString(EXPLORATEUR_KEY,token).apply()
    }

    fun handleNoConnectionError(view:View){
        hideKeyboard()
        mSnackbar = Snackbar.make(mainContainer!!, "Connexion au serveur impossible", Snackbar.LENGTH_INDEFINITE)
                .setAction("Réessayer") {
                    onLoginClick(view)
                }
        mSnackbar!!.show()
    }


    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun onCreateAccountClick(view: View){
        if(view.id == R.id.login_btn_signup){
            val intent = Intent(this,CreationCompteActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        }
    }


}
