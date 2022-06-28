package com.mohamed.medhat.photoweather.utils

import android.graphics.*
import com.mohamed.medhat.photoweather.model.SimpleWeatherData
import javax.inject.Inject


/**
 * Used to edit the images.
 */
class PhotoEditor @Inject constructor() {

    /**
     * Adds a weather banner that contains some weather data to an image.
     * @param data The object that contains the weather data.
     * @param path The path of the photo to edit.
     * @return The modified image.
     */
    fun addWeatherBanner(data: SimpleWeatherData, path: String): Bitmap {
        val bitmap = BitmapFactory.decodeFile(path)
        val textPaint = Paint()
        textPaint.style = Paint.Style.FILL
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.textSize = 60f

        val mutableBitmap = bitmap.copy(bitmap.config, true)
        val canvas = Canvas(mutableBitmap)

        val backgroundPaint = Paint()
        backgroundPaint.color = Color.BLACK
        backgroundPaint.alpha = 128
        backgroundPaint.style = Paint.Style.FILL

        val lineRect = Rect()
        textPaint.getTextBounds("A", 0, 1, lineRect)

        val lineHeight = lineRect.height().toFloat()
        canvas.drawRect(
            0f,
            0f,
            canvas.width.toFloat(),
            (lineHeight + 40) * 3 + 40,
            backgroundPaint
        )

        canvas.printTextLine("${data.sys.country}, ${data.name}", 1, lineHeight, textPaint)

        if (data.weather.isNotEmpty()) {
            canvas.printTextLine(data.weather[0].description, 2, lineHeight, textPaint)
        }

        canvas.printTextLine("${data.main.temp}°C", 3, lineHeight, textPaint)

        return mutableBitmap
    }

    /**
     * Draws a text on a specific line of the canvas.
     * @param text The text to draw.
     * @param lineNumber Which line to draw on.
     * @param lineHeight The height of the number.
     * @param paint The [Paint] object describing how the text should look like.
     */
    private fun Canvas.printTextLine(
        text: String,
        lineNumber: Int,
        lineHeight: Float,
        paint: Paint
    ) {
        this.drawText(
            text,
            40f,
            lineHeight * lineNumber + 40 * lineNumber,
            paint
        )
    }
}