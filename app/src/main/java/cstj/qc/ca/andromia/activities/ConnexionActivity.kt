package cstj.qc.ca.andromia.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.models.Unit
import cstj.qc.ca.andromia.models.Validator
import kotlinx.android.synthetic.main.activity_connexion.*

class ConnexionActivity : AppCompatActivity(){

    //private var mlstUnit = mutableListOf<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

// Only for testing purposes
//        "https://andromia-damax55.c9users.io/explorateurs/auhdasiuhdfasdf/units"!!.httpGet().responseJson { _, response, result ->
//            if (response.httpStatusCode == 200) {
//                createUnitList(result.get())
//                if(mlstUnit.size > 0){
//                    Toast.makeText(this, mlstUnit[0].name, LENGTH_SHORT).show()
//                    Toast.makeText(this, mlstUnit[0].affinitiy, LENGTH_SHORT).show()
//                    Toast.makeText(this, mlstUnit[0].href, LENGTH_SHORT).show()
//                    Toast.makeText(this, mlstUnit[0].imageURL, LENGTH_SHORT).show()
//                    Toast.makeText(this, mlstUnit[0].life, LENGTH_SHORT).show()
//                    Toast.makeText(this, mlstUnit[0].number, LENGTH_SHORT).show()
//                    Toast.makeText(this, mlstUnit[0].set, LENGTH_SHORT).show()
//                    Toast.makeText(this, mlstUnit[0].speed, LENGTH_SHORT).show()
//                }
//            }
//        }
    }

//    fun createUnitList(json: Json){
//
//        val tabJson = json.array()
//        mlstUnit.clear()
//        for(i in 0.. (tabJson.length() -1 )) {
//            mlstUnit.add(Unit(Json(tabJson[i].toString())))
//        }
//    }




    fun onLoginClick(view:View){
        if(view.id == R.id.login_btn_signin){
            if(Validator.emailValidator(login_et_email.text.toString())){
                if(login_et_password.length() > 0){
                    Toast.makeText(this,"OK!",LENGTH_SHORT).show()
                    var intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
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
