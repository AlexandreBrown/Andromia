package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json
import org.json.JSONObject


class Exploration(jsonObj: Json): Item() {

    val locationDepart:String = jsonObj.obj().getString("locationDepart")
    val locationDestination:String = jsonObj.obj().getString("locationDestination")
    val dateExploration:String = formatDate(jsonObj.obj().getString("dateExploration"))

    private  fun formatDate(date:String): String {
        var dateFormatee:String = date
        dateFormatee = dateFormatee.replace("T"," ")
        dateFormatee = dateFormatee.substring(0,dateFormatee.length-5)
        return dateFormatee
    }

    override fun getAffichage(): Array<String> {

        val tabAfficher = arrayOf(locationDepart, locationDestination, dateExploration)

        return tabAfficher

    }
}