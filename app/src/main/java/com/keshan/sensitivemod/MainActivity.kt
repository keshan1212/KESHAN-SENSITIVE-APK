
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

        val root = FrameLayout(this)
        root.setBackgroundColor(Color.rgb(5, 8, 25))
        root.addView(LightningView(this))

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(20, 25, 20, 25)
        }

        scroll.addView(content)
        root.addView(scroll)
        setContentView(root)

        content.addView(label("⚡ KESHAN SENSITIVE MOD ⚡", 25, "#00E5FF", true))
        content.addView(label("FREE FIRE SENSITIVITY GUIDE", 15, "#FFFFFF", true))
        content.addView(label("ඔයාගේ Phone එකට ගැළපෙන Settings", 14, "#FFFFFF", false))

        content.addView(label("SELECT YOUR PHONE", 18, "#FFE45E", true))

        val brands = listOf(
            "Samsung",
            "Xiaomi / Redmi",
            "OPPO",
            "vivo",
            "Realme",
            "Other Android"
        )

        brands.forEach { brand ->
            content.addView(makeButton(brand) {
                selectedPhone = brand
                updateValues()
            })
        }

        content.addView(label("SENSITIVITY SETTINGS", 19, "#00E5FF", true))

        valuesText = label("", 17, "#FFFFFF", true)
        valuesText.setBackgroundColor(Color.rgb(15, 25, 55))
        content.addView(valuesText)

        updateValues()

        content.addView(makeButton("📋 COPY SETTINGS") {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            clipboard.setPrimaryClip(
                ClipData.newPlainText("Sensitivity", valuesText.text)
            )

            Toast.makeText(
                this,
                "Settings Copied!",
                Toast.LENGTH_SHORT
            ).show()
        })

        content.addView(label("⚡ LIVE LIGHTNING BACKGROUND", 17, "#FFE45E", true))

        content.addView(
            label(
                "මෙම Settings ආරම්භක නිර්දේශ පමණයි. " +
                "ඔබේ Phone එක සහ Game Version එක අනුව වෙනස් කරගන්න.",
                13,
                "#FFFFFF",
                false
            )
        )

        content.addView(
            label(
                "KESHAN SENSITIVE MOD • VERSION 1.0",
                12,
                "#00E5FF",
                true
            )
        )
    }

    private fun updateValues() {
        valuesText.text = """
            $selectedPhone

            General: 95
            Red Dot: 90
            2x Scope: 85
            4x Scope: 80
            Sniper Scope: 50
            Free Look: 75
        """.trimIndent()
    }

    private fun label(
        text: String,
        size: Int,
        color: String,
        bold: Boolean
    ): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size.toFloat()
            setTextColor(Color.parseColor(color))
            gravity = Gravity.CENTER
            setPadding(8, 12, 8, 12)

            if (bold) {
                typeface = Typeface.DEFAULT_BOLD
            }
        }
    }

    private fun makeButton(
        text: String,
        action: () -> Unit
    ): Button {
        return Button(this).apply {
            this.text = text
            textSize = 14f
            isAllCaps = false
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(27, 43, 88))
            setOnClickListener { action() }

            layoutParams = LinearLayout.LayoutParams(
                -1,
                58
            ).apply {
                setMargins(0, 5, 0, 5)
            }
        }
    }
}

class LightningView(context: Context) : View(context) {

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

    init {
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.rgb(6, 10, 29))

        val w = width.toFloat()
        val h = height.toFloat()

        for (i in 0..2) {

            val x = w * (0.16f + i * 0.34f)
            val shift = ((phase + i * 0.31f) % 1f) * h

            val path = Path().apply {
                moveTo(x, shift - h * 0.22f)
                lineTo(x - w * 0.07f, shift - h * 0.08f)
                lineTo(x + w * 0.025f, shift)
                lineTo(x - w * 0.055f, shift + h * 0.13f)
            }

            paint.color = if (i % 2 == 0) {
                Color.argb(145, 0, 190, 255)
            } else {
                Color.argb(120, 155, 70, 255)
            }

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
