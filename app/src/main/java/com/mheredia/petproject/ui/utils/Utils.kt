package com.mheredia.petproject.ui.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

class Utils {
companion object
     fun sendMessage(view: View, message:String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }


}