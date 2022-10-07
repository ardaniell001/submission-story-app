package com.ardanil.submissionstoryapp.config

import com.ardanil.submissionstoryapp.view.home.StoryViewHolder
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
	private const val TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
	private const val DATE_FULL_FORMAT = "dd MMM yyyy HH:mm:ss"
	private const val LOCALE_INDONESIA = "in"
	private const val UTC_TIMEZONE = "UTC"

	internal fun stringToDate(dateString: String): Date? {
		return SimpleDateFormat(TIME_STAMP_FORMAT, Locale(LOCALE_INDONESIA)).apply {
			timeZone = TimeZone.getTimeZone(UTC_TIMEZONE)
		}.parse(dateString)
	}

	internal fun dateToString(date: Date): String {
		val dateFormat = SimpleDateFormat(DATE_FULL_FORMAT, Locale(LOCALE_INDONESIA))
		return dateFormat.format(date)
	}

}