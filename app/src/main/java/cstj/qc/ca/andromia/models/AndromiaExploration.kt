package cstj.qc.ca.andromia.models

import com.github.kittinunf.fuel.android.core.Json
import org.json.JSONObject


class AndromiaExploration(jsonObj: Json) {
    val dateExploration:String = jsonObj.obj().getString("dateExploration")
    val destination:String = jsonObj.obj().getString("destination")
    val runes: JSONObject = jsonObj.obj().getJSONObject("runes")
    val unit: JSONObject = jsonObj.obj().getJSONObject("unit")
}