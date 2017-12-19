package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json


class Explorateur (jsonObj: Json) {
    val courriel:String = jsonObj.obj().getString("courriel")
    val nom:String = jsonObj.obj().getString("nom")
    val inox:Int = jsonObj.obj().getInt("inox")
    val location:String = jsonObj.obj().getString("location")
    val unitsHref:String = jsonObj.obj().getJSONObject("units").getString("href")
    var lstRunes = jsonToMap(jsonObj)

    private fun jsonToMap(json: Json):MutableList<Rune> {
        var runes = mutableListOf<Rune>()
        for (key in json.obj().getJSONObject("runes").keys()) {
            runes.add(Rune(key, (json.obj().getJSONObject("runes")[key]).toString().toInt()))
        }
        return runes
    }
}

