package com.eliadp.androidshowcase.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafely(directions: NavDirections) {
    try {
        this.navigate(directions)
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
}
