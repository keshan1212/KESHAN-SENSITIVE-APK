package com.keshan.sensitivemod

import android.app.Activity
import android.os.Bundle
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.animation.ValueAnimator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.graphics.Typeface

class MainActivity : Activity() {
    private var selectedPhone = "Other Android"
    private lateinit var valuesText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = FrameLayout(this).apply {
            setBackgroundColor(Color.rgb(5, 8, 25))
        }
        root.addView(LightningView(this), FrameLayout.LayoutParams(-1, -1))

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(20), dp(26), dp(20), dp(28))
        }
        scroll.addView(content)
        root.addView(scroll)
        setContentView(root)

        val title = label("⚡ KESHAN SENSITIVE MOD ⚡", 25, "#00E5FF", true)
        content.addView(title)
        content.addView(label("FREE FIRE SENSITIVITY GUIDE", 13, "#FFFFFF", true))
        content.addView(label("ඔයාගේ Phone එකට ගැළපෙන ආරම්භක Settings", 14, "#C9D8FF", false))

        content.addView(space())
        content.addView(label("SELECT YOUR PHONE BRAND", 16, "#FFE45E", true))
        content.addView(label("ඔයාගේ දුරකථන වර්ගය තෝරන්න", 13, "#FFFFFF", false))

        val brands = listOf("Samsung", "Xiaomi / Redmi", "OPPO", "vivo", "Realme", "Other Android")
        val brandRow = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        brands.forEach { brand ->
            val b = makeButton(brand) {
                selectedPhone = brand
                updateValues()
            }
            brandRow.addView(b)
        }
        content.addView(brandRow)

        content.addView(space())
        content.addView(label("SENSITIVITY PRESET", 17, "#00E5FF", true))
        valuesText = label("", 16, "#FFFFFF", true)
        valuesText.setBackgroundColor(Color.argb(190, 12, 19, 48))
        valuesText.setPadding(dp(16), dp(16), dp(16), dp(16))
        content.addView(valuesText)
        updateValues()

        content.addView(makeButton("📋  COPY SETTINGS / පිටපත් කරන්න") {
            val clip = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clip.setPrimaryClip(ClipData.newPlainText("Sensitivity", valuesText.text))
            Toast.makeText(this, "Settings copied!", Toast.LENGTH_SHORT).show()
        })

        content.addView(space())
        content.addView(label("⚡ LIGHTNING ANIMATION", 17, "#FFE45E", true))
        content.addView(label("Animated background is running behind this screen.", 12, "#DDE5FF", false))
        content.addView(label("Note: These are starting-point suggestions, not guaranteed headshot settings. Adjust in training mode to suit your device, FPS and touch response. This app does not modify Free Fire or provide hacks/aimlock.", 12, "#C7D0E8", false))
        content.addView(label("KESHAN SENSITIVE MOD • VERSION 1.0", 11, "#00E5FF", true))
    }

    private fun updateValues() {
        valuesText.text = "$selectedPhone\n\nGeneral: 95\nRed Dot: 90\n2x Scope: 85\n4x Scope: 80\nSniper Scope: 50\nFree Look: 75\n\nමෙය ආරම්භක preset එකක් පමණයි. ඔබේ phone එකේ game settings තුළ අතින් සකස් කරන්න."
    }

    private fun label(text: String, size: Int, color: String, bold: Boolean): TextView =
        TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(Color.parseColor(color))
            gravity = Gravity.CENTER
            setPadding(dp(5), dp(7), dp(5), dp(7))
            if (bold) typeface = Typeface.DEFAULT_BOLD
        }

    private fun makeButton(text: String, action: () -> Unit) = Button(this).apply {
        this.text = text
        textSize = 14f
        setTextColor(Color.WHITE)
        setBackgroundColor(Color.rgb(27, 43, 88))
        isAllCaps = false
        setOnClickListener { action() }
        val p = LinearLayout.LayoutParams(-1, dp(52))
        p.setMargins(0, dp(4), 0, dp(4))
        layoutParams = p
    }

    private fun space() = View(this).apply {
        layoutParams = LinearLayout.LayoutParams(1, dp(12))
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}

private class LightningView(context: Context) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var phase = 0f
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 900
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            phase = it.animatedValue as Float
            invalidate()
        }
    }

    init { animator.start() }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        paint.color = Color.rgb(6, 10, 29)
        canvas.drawColor(paint.color)

        // Subtle animated electric streaks; decorative only.
        for (i in 0..2) {
            val x = w * (0.16f + i * 0.34f)
            val shift = ((phase + i * 0.31f) % 1f) * h
            val path = Path().apply {
                moveTo(x, shift - h * 0.22f)
                lineTo(x - w * 0.07f, shift - h * 0.08f)
                lineTo(x + w * 0.025f, shift)
                lineTo(x - w * 0.055f, shift + h * 0.13f)
            }
            paint.color = if (i % 2 == 0) Color.argb(145, 0, 190, 255) else Color.argb(120, 155, 70, 255)
            paint.strokeWidth = 3f
            paint.style = Paint.Style.STROKE
            canvas.drawPath(path, paint)
        }
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }
}
