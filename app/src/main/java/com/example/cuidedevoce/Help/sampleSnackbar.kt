package com.example.cuidedevoce.Help

import android.view.View
import com.google.android.material.snackbar.Snackbar

object sampleSnackbar{
    fun sampleSnackbar(charSequence: CharSequence, binding: View){
        Snackbar.make(
            binding,
            charSequence,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}