package cstj.qc.ca.andromia.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_creation_compte.*
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences



class CreationCompteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_compte)
    }

    fun onSignupClick(view: View){
        if(view.id == R.id.signup_btn_signup){
            if(signup_et_name.length() > 0){
                if(Validator.emailValidator(signup_et_email.text.toString())){
                    if(signup_et_password.length() > 0){
                        if(signup_et_confirm_password.length() > 0){
                            if(signup_et_password.text.toString().equals(signup_et_confirm_password.text.toString())){
                                val key = "FCCB35B2542B878D86E8CC25773E4"
                                val prefs = this.getSharedPreferences("cstj.qc.ca.andromia.explorateur", Context.MODE_PRIVATE)
                                prefs.edit().putString("cstj.qc.ca.andromia.explorateur",key).apply()
                                val keyRetrieved = prefs.getString("cstj.qc.ca.andromia.explorateur","")
                                Toast.makeText(this,"Explorateur : $keyRetrieved", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                                this.finish()
                            }else{
                                signup_et_confirm_password.setError("Les mots de passe ne sont pas identiques")
                            }
                        }else{
                            signup_et_confirm_password.setError("Le mot de passe est invalide")
                        }
                    }else{
                        signup_et_password.setError("Le mot de passe est invalide")
                    }
                }else{
                    signup_et_email.setError("Le courriel est invalide")
                }
            }else{
                signup_et_name.setError("Le nom est invalide")
            }
        }
    }
}
