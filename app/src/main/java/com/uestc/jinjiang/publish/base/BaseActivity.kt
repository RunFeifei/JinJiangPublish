package com.uestc.run.basebase

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.uestc.jinjiang.publish.R

/**
 * Created by PengFeifei on 2018/10/27.
 */

abstract class BaseActivity : AppCompatActivity() {


    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


}


fun Context.getDrawable2(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

fun Fragment.getDrawable(@DrawableRes id: Int): Drawable? {
    return requireContext().getDrawable2(id)
}
