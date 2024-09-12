package com.quinta.namasteindia.utils

import android.R
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

class CustomLoader(context: Context) : Dialog(context, R.style.ThemeOverlay_Material_Dialog) {

    init {
        setCancelable(false)
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val progressBar = ProgressBar(context)
        progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(context, R.color.holo_blue_light),
            android.graphics.PorterDuff.Mode.SRC_IN
        )

        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.CENTER_IN_PARENT)

        val layout = RelativeLayout(context)
        layout.layoutParams = params
        layout.addView(progressBar)

        setContentView(layout)
    }
}
