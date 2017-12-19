package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json


class Exploration(jsonObj: Json): Item() {

    val locationDepart:String = jsonObj.obj().getString("locationDepart")
    val locationDestination:String = jsonObj.obj().getString("locationDestination")
    val dateExploration:String = jsonObj.obj().getString("dateExploration")

    override fun getAffichage(): Array<String> {

        val tabAfficher = arrayOf(locationDepart, locationDestination, dateExploration)

        return tabAfficher

    }
}