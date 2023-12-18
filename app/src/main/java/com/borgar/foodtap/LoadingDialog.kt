package com.borgar.foodtap

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog {

    private val activity: Activity
    private var dialog: AlertDialog

    constructor(myactivity: Activity){
        activity = myactivity
        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        var inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
    }

    fun startLoadingDialog(){

        dialog.show()
    }

    fun dismissLoadingDialog() = dialog.dismiss()

}