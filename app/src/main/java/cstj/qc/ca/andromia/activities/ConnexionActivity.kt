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
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpPost
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*
import org.json.JSONObject


class ConnexionActivity : AppCompatActivity(){

    private var mSnackbar:Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

    }

    fun onLoginClick(view:View){
        if(view.id == R.id.login_btn_signin){
            if(Validator.emailValidator(login_et_email.text.toString())){
                if(login_et_password.length() > 0 ){
                    var explorateurJSON = JSONObject()
                    explorateurJSON.put("username",login_et_email.text.toString())
                    explorateurJSON.put("password",login_et_password.text.toString())

                    connecterExplorateur(explorateurJSON)
                }else{
                    login_et_password.error = "Le mot de passe ne peut pas être vide"
                }
            }else{
                login_et_email.error = "Le format du courriel est invalide"
            }
        }
    }

    fun connecterExplorateur(explorateurJSON:JSONObject){
        val request = (BASE_URL+"connexion").httpPost()
        request.httpHeaders["Content-Type"] = "application/json"
        request.httpBody = explorateurJSON.toString().toByteArray()
        request.responseJson{ _, response, result ->
            when{ // Si le courriel et mot de passe est bon
                (response.httpStatusCode == 200) ->{
                    hideKeyboard(container_login)
                    createToken(result.get().obj().getString("token"))
                    // On change d'activity
                    val intent = Intent(this,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("hrefExplorateur",result.get().obj().getJSONObject("user").getString("href").removePrefix(BASE_URL+"explorateurs/"))
                    startActivity(intent)
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    this.finish()
                }
                (response.httpStatusCode in 400..499) -> {
                    if(mSnackbar != null){
                        mSnackbar!!.dismiss()
                    }
                    login_et_password.text.clear()
                    hideKeyboard(container_login)
                    Toast.makeText(this,"Votre courriel ou mot de passe est invalide",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    handleNoConnectionError(container_login)
                }
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
        hideKeyboard(view)
        mSnackbar = Snackbar.make(view, "Connexion au serveur impossible", Snackbar.LENGTH_INDEFINITE)
                .setAction("Réessayer") {
                    onLoginClick(view)
                }
        mSnackbar!!.show()
    }


    fun hideKeyboard(v:View){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun onCreateAccountClick(view: View){
        if(view.id == R.id.login_btn_signup){
            val intent = Intent(this,CreationCompteActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        }
    }


}
