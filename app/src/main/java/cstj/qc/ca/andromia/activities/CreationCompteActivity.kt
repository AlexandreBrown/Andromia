package cstj.qc.ca.andromia.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_creation_compte.*
import org.json.JSONObject


class CreationCompteActivity : AppCompatActivity() {

    private var mSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_compte)
    }

    fun hideKeyboard(v:View){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun onSignupClick(view: View){
        if(view.id == R.id.signup_btn_signup){
            if(signup_et_name.length() > 0){
                if(Validator.emailValidator(signup_et_email.text.toString())){
                    if(signup_et_password.length() > 0){
                        if(signup_et_confirm_password.length() > 0){
                            if(signup_et_password.text.toString() == signup_et_confirm_password.text.toString()){
                                var explorateurJSON = JSONObject()
                                explorateurJSON.put("nom",signup_et_name.text.toString())
                                explorateurJSON.put("courriel",signup_et_email.text.toString())
                                explorateurJSON.put("motDePasse",signup_et_password.text.toString())

                                val request = (BASE_URL +"inscription").httpPost()
                                request.httpHeaders["Content-Type"] = "application/json"
                                request.httpBody = explorateurJSON.toString().toByteArray()
                                request.responseJson{ _, response, _ ->
                                    when{
                                        // Si l'insertion à fonctionnée
                                        (response.httpStatusCode == 201) ->{
                                            var explorateurJSON = JSONObject()
                                            explorateurJSON.put("username",signup_et_email.text.toString())
                                            explorateurJSON.put("password",signup_et_password.text.toString())

                                            connecterExplorateur(explorateurJSON)
                                        }
                                        (response.httpStatusCode in 400..499) -> {
                                            if(mSnackbar != null){
                                                mSnackbar!!.dismiss()
                                            }
                                            signup_et_password.text.clear()
                                            signup_et_confirm_password.text.clear()
                                            hideKeyboard(container_signup)
                                            Toast.makeText(this,"Votre courriel ou mot de passe est invalide",Toast.LENGTH_SHORT).show()
                                        }
                                        else -> {
                                            handleNoConnectionError(container_signup)
                                        }
                                    }
                                }
                            }else{
                                signup_et_confirm_password.setError("Les mots de passe ne sont pas identiques")
                            }
                        }else{
                            signup_et_confirm_password.setError("Veuillez confirmer votre mot de passe")
                        }
                    }else{
                        signup_et_password.setError("Le mot de passe ne peut pas être vide")
                    }
                }else{
                    signup_et_email.setError("Le format du courriel est invalide")
                }
            }else{
                signup_et_name.setError("Le nom ne peut pas être vide")
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
                    hideKeyboard(container_signup)
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
                    signup_et_password.text.clear()
                    signup_et_confirm_password.text.clear()
                    hideKeyboard(container_signup)
                    Toast.makeText(this,"Votre courriel ou mot de passe est invalide",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    handleNoConnectionError(container_signup)
                }
            }
        }
    }

    fun handleNoConnectionError(view:View){
        hideKeyboard(view)
        mSnackbar = Snackbar.make(view, "Connexion au serveur impossible", Snackbar.LENGTH_INDEFINITE)
                .setAction("Réessayer") {
                    onSignupClick(signup_btn_signup)
                }
        mSnackbar!!.show()
    }

    fun createToken(token:String){
        // On récupère les SharedPreferences
        val prefs = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        // On ajoute le token dans les SharedPreferences
        prefs.edit().putString(EXPLORATEUR_KEY,token).apply()
    }
}
