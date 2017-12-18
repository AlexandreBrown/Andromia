package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json


class Explorateur (jsonObj: Json) {
    val courriel:String = jsonObj.obj().getString("courriel")
    val nom:String = jsonObj.obj().getString("nom")
    val inox:Int = jsonObj.obj().getInt("inox")
    val location:String = jsonObj.obj().getString("location")
    val unitsHref:String = jsonObj.obj().getJSONObject("units").getString("href")
    val href:String = jsonObj.obj().getString("href")
}