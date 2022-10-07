package com.ardanil.submissionstoryapp.config

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {

	private fun getTimeStamp(): String {
		return java.text.SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(Date())
	}

	private fun createTempFile(context: Context): File {
		val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
		return File.createTempFile(getTimeStamp(), ".jpg", storageDir)
	}

	internal fun createImageFile(context: Context): File? {
		val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
		val imageFileName = "JPEG_" + timeStamp + "_"
		val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
		return File.createTempFile(imageFileName, ".jpg", storageDir)
	}

	fun uriToFile(uri: Uri, context: Context): File {
		val contentResolver: ContentResolver = context.contentResolver
		val myFile = createTempFile(context)
		val inputStream = contentResolver.openInputStream(uri) as InputStream
		val outputStream: OutputStream = FileOutputStream(myFile)
		val buf = ByteArray(1024)
		var len: Int
		while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
		outputStream.close()
		inputStream.close()
		return myFile
	}

	fun compressImage(file: File): File {
		val bitmap = BitmapFactory.decodeFile(file.path)
		var compressQuality = 100
		var streamLength: Int
		do {
			val bmpStream = ByteArrayOutputStream()
			bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
			val bmpPicByteArray = bmpStream.toByteArray()
			streamLength = bmpPicByteArray.size
			compressQuality -= 5
		} while (streamLength > 1000000)
		bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

		return file
	}
}