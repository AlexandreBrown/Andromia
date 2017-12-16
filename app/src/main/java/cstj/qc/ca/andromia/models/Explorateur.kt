package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json


class Explorateur (jsonObj: Json) {
    val nom:String = jsonObj.obj().getString("nom")
    val courriel:String = jsonObj.obj().getString("courriel")
    val inox:Int = jsonObj.obj().getInt("inox")
    val location:String = jsonObj.obj().getString("location")
}