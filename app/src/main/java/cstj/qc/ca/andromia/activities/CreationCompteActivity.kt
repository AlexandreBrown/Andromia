package cstj.qc.ca.andromia.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_creation_compte.*

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
                                Toast.makeText(this,"OK!", Toast.LENGTH_SHORT).show()

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
