package cstj.qc.ca.andromia.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import cstj.qc.ca.andromia.R
import cstj.qc.ca.andromia.activities.ConnexionActivity
import cstj.qc.ca.andromia.adapters.RuneRecyclerViewAdapter
import cstj.qc.ca.andromia.helpers.BASE_URL
import cstj.qc.ca.andromia.helpers.EXPLORATEUR_KEY
import cstj.qc.ca.andromia.helpers.PREF_KEY
import cstj.qc.ca.andromia.models.Explorateur
import kotlinx.android.synthetic.main.dialog_runes.view.*
import android.view.Gravity
import android.widget.TextView



class RunesDialog : DialogFragment(){


    private lateinit var inflater:LayoutInflater
    private lateinit var dialogView:View
    private var mHrefExplorateur:String? = null
    private  var mListener:OnRunesDialogListener? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            mHrefExplorateur = it.getString(RunesDialog.ARG_HREF)
        }

        inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_runes,null)

        dialogView.dialog_runes_btn_fermer.setOnClickListener {
            mListener!!.onFermerDialog()
        }

        val prefs = activity!!.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        val token:String = prefs.getString(EXPLORATEUR_KEY, "")
        if(token.isNotEmpty() && !mHrefExplorateur!!.isBlank()){
            var request = (BASE_URL +"explorateurs/$mHrefExplorateur").httpGet().header("Authorization" to "Bearer $token")
            request.responseJson{ _, response, result ->
                when{
                    (response.httpStatusCode == 200) ->{
                        val explorateur = Explorateur(result.get())
                        if(dialogView.list is RecyclerView){
                            dialogView.list.adapter = RuneRecyclerViewAdapter(explorateur.lstRunes,mListener)
                            dialogView.list.layoutManager = LinearLayoutManager(context)
                            dialogView.list.adapter.notifyDataSetChanged()
                        }
                    }
                    else -> {
                        logout()
                    }
                }
            }
        }else{
            logout()
        }
        var builder:AlertDialog.Builder = AlertDialog.Builder(activity,R.style.DialogTheme)

        val title = TextView(this.context)
        title.text = "Mes runes"
        title.setPadding(10, 10, 10, 10)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.BLACK)
        title.textSize = 20F

        builder
                .setCustomTitle(title)
                .setView(dialogView)

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OnRunesDialogListener){
            mListener = context as OnRunesDialogListener
        }else{
            throw RuntimeException(context.toString() + " must implement OnRunesDialogListener")
        }
    }


    private fun logout(){
        val intent = Intent(this.context, ConnexionActivity::class.java)

        // Suppression du token
        val myPrefs = this.activity!!.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        myPrefs.edit().remove(EXPLORATEUR_KEY).apply()

        // Retour à l'écran de connexion
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.activity!!.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
        this.activity!!.finish()
    }


    interface OnRunesDialogListener {
        fun onFermerDialog()
    }

    companion object {

        private var ARG_HREF :String? = null

        @JvmStatic
        fun newInstance(href:String): RunesDialog =
                RunesDialog().apply {
                    arguments = Bundle().apply {
                        putString(ARG_HREF,href)
                    }
                }
    }

}