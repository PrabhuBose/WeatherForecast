package com.senthil.prabhu.android.weatherforecast.utils

import android.app.Activity
import com.rw.loadingdialog.LoadingView
import com.senthil.prabhu.android.weatherforecast.R


class CustomProgressDialog(activity: Activity?) {

    private var loadingView: LoadingView? = null

    init {
        try {
            loadingView = LoadingView.Builder(activity)
                .setProgressColorResource(R.color.colorPrimaryDark)
                .setBackgroundColorRes(android.R.color.transparent)
                .setProgressStyle(LoadingView.ProgressStyle.CYCLIC)
                .attachTo(activity)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun show() {
        loadingView!!.show()

    }

    fun hide() {
        loadingView!!.hide()
    }
}
