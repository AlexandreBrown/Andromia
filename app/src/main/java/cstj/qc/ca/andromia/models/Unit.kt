package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json
import org.json.JSONArray
import org.json.JSONObject

class Unit (jsonObj: Json): Item(){
    val href:String = jsonObj.obj().getString("href")
    val name:String = jsonObj.obj().getString("name")
    val life:String = jsonObj.obj().getString("life")
    val speed:String = jsonObj.obj().getString("speed")
    val imageURL:String = jsonObj.obj().getString("imageURL")
    val affinity:String = jsonObj.obj().getString("affinity")
    val set:String = jsonObj.obj().getString("set")
    val number:String = jsonObj.obj().getString("number")
    val runes:JSONObject = jsonObj.obj().getJSONObject("runes")
    val abilities:JSONArray = runes.getJSONArray("abilities")
    val weapons:JSONArray = runes.getJSONArray("weapons")
    val ultimate:String = runes.getString("ultimate")
    val kernel:JSONObject = jsonObj.obj().getJSONObject("kernel")


    override fun getAffichage(): Array<String> {

        val tabAfficher = arrayOf(name, number, set, imageURL)

        return tabAfficher

    }
}