package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json

class Unit (jsonObj: Json): Item(){
    val href:String = jsonObj.obj().getString("href")
    val name:String = jsonObj.obj().getString("name")
    val life:String = jsonObj.obj().getString("life")
    val speed:String = jsonObj.obj().getString("speed")
    val imageURL:String = jsonObj.obj().getString("imageURL")
    val affinitiy:String = jsonObj.obj().getString("affinity")
    val set:String = jsonObj.obj().getString("set")
    val number:String = jsonObj.obj().getString("number")
    val runes = jsonObj.obj().get("runes")
    //val weapons:List<String> = jsonObj.obj().accumulate("runes","weapons")
    //val abilities = {}


    override fun getAffichage(): Array<String> {

        val tabAfficher = arrayOf(name, imageURL, set, number)

        return tabAfficher

    }
}