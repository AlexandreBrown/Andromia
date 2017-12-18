package cstj.qc.ca.andromia.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import cstj.qc.ca.andromia.R

class RunesDialog : DialogFragment() {

    private lateinit var inflater:LayoutInflater
    private lateinit var dialogView:View



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        inflater = activity.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_runes,null)

        var builder:AlertDialog.Builder = AlertDialog.Builder(activity)

        builder
                .setMessage("Dialog text here")
                .setTitle("Mes runes")
                .setView(dialogView)

        var dialog:AlertDialog = builder.create()

        return dialog

    }
}