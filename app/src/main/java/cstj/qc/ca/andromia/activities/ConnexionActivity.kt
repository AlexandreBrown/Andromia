package cstj.qc.ca.andromia.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*

class ConnexionActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }



    fun onLoginClick(view:View){
        if(view.id == R.id.login_btn_signin){
            if(Validator.emailValidator(login_et_email.text.toString())){
                if(login_et_password.length() > 0){
                    Toast.makeText(this,"OK!",LENGTH_SHORT).show()
                }else{
                    login_et_password.setError("Le mot de passe est invalide")
                }
            }else{
                login_et_email.setError("Le courriel est invalide")
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
