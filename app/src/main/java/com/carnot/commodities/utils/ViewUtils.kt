package com.carnot.commodities.utils

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Patterns
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ViewUtils {
    companion object {
        @SuppressLint("MissingPermission")
        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            if (!activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                return false
            }
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            return result
        }

        fun getCurrentDate(isTimeRequired: Boolean = false): String {
            val dateFormat = if (isTimeRequired) {
                SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss", Locale.getDefault()
                )

            } else {
                SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault()
                )
            }
            val date = Date()
            return dateFormat.format(date)
        }
    }
}

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}


fun Context.showToast(message: String) {
    try {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Disable the view for the time range provided as a parameter thus preventing the problem of double actions such as multiple dialogs, etc.
 * @param delayTime Time in milli seconds
 */
fun View.preventDoubleClick(delayTime: Long = 1000) {
    this.apply { isEnabled = false }.postDelayed({ isEnabled = true }, delayTime)
}

fun EditText.removeFocus() {
    if (this.hasFocus()) {
        this.clearFocus()
        this.context.hideKeyboard(this)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackBar ->
        snackBar.setAction("Ok") {
            snackBar.dismiss()
        }
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    }.show()
}

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.makeFirstLetterCapital(): String {
    var foundSpace = true
    val arr = this.toCharArray()
    for (i in arr.indices) {
        if (foundSpace) {
            arr[i] = Character.toUpperCase(arr[i])
            foundSpace = false
        } else {
            foundSpace = arr[i] == ' '
        }
    }
    return String(arr)
}

/**
 * This functions converts server date to any format given
 * @param newFormat -> Any desired format eg. "dd, MMM yyyy"
 * @return Formatted date in string type eg. 01, Jan 2022
 */
fun String.convertServerDateToNormal(newFormat: String): String? {
    var date = this
    var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    spf.timeZone = TimeZone.getDefault()
    var newDate: Date? = null
    try {
        newDate = spf.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    spf = SimpleDateFormat(newFormat, Locale.getDefault())
    date = spf.format(newDate)
    return date
}

/**
 * This performs loading an image from the given URL using Glide
 * @param url Image links such as bank, user profile, etc.

fun ImageView.loadImage(url : String) {
Glide.with(this).load(url)
.error(R.drawable.ic_logo_x)
.placeholder(R.drawable.ic_logo_x)
.into(this)
}*/

/**
 * This function converts an integer value to an indian currency by adding rupee symbol at
 * the end of the result also keeping only value before the decimal point
 * @return String eg. Value = 100 -> Result = "₹ 100.0000" -> finalResult = "₹ 100"
 */
fun Int.convertToCurrency(): String = NumberFormat.getCurrencyInstance(Locale("en", "in")).apply {
    maximumFractionDigits = 0
}.format(this)


/**
 * Converts server date to long format that'll be used to set the max date for the date picker
 * @return Date in long datatype
 */
fun String.getDateInLongFormat(): Long {
    val spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    spf.timeZone = TimeZone.getDefault()
    val newDate: Date?
    var dateInLong = 0L
    try {
        newDate = spf.parse(this)
        dateInLong = newDate.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return dateInLong
}

/**
 * Get calculated date by passing any number of days to this function
 * @param days Number of days to be subtracted from the current date
 *  @return Date in string format, eg. days = 7 & Current Date = 08-02-2021 then result = 01-02-2021
 */
fun getCalculatedDate(days: Int): String {
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    calendar.add(Calendar.DAY_OF_YEAR, days)
    return formatter.format(Date(calendar.timeInMillis))
}

/*
 * This ensure if the user has clicked a button multiple times to navigate to a different screen without
 * throwing illegal argument exception error saying cannot find specified navigation action.
 * @param directions Passing generated directions classes with or without arguments
 * defined in the mobile_navigation.xml file.
 */
//fun NavController.safeNavigate(directions: NavDirections) {
//    currentDestination?.getAction(directions.actionId)?.run {
//        navigate(directions)
//    }
//}


fun View.startRotatingAnimation() {
    val animator = ObjectAnimator.ofFloat(this, "rotationY", 0.0F, 360F)
    animator.duration = 2500
    animator.repeatCount = ObjectAnimator.INFINITE
    animator.repeatMode = ObjectAnimator.REVERSE
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.start()
}

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                textPaint.color = textPaint.linkColor
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
        if (startIndexOfLink == -1) continue //if you want to verify your texts contains links text
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TextView.colorAsteriskText(length: Int) {
    text = SpannableString(text).apply {
        setSpan(
            ForegroundColorSpan(Color.RED),
            length - 1,
            length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun View.openDatePickerDialog(fragmentManager: FragmentManager): String {
    val constraintsBuilder =
        CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    var date = ""
    datePicker.show(fragmentManager, "DOB")
    datePicker.addOnPositiveButtonClickListener {
        date = datePicker.headerText.replace(" ", " / ")
    }
    return date
}