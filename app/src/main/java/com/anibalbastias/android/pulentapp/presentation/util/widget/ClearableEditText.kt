package com.anibalbastias.android.pulentapp.presentation.util.widget

/**
 * Created by anibalbastias on 2019-05-10.
 */

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.widget.EditText
import com.anibalbastias.android.pulentapp.R

/**
 * To clear icon can be changed via
 *
 * <pre>
 * android:drawable(Right|Left)="@drawable/custom_icon"
</pre> *
 */

open class ClearableEditText : EditText,
        OnTouchListener,
        OnFocusChangeListener,
    TextWatcherAdapter.TextWatcherListener {

    private var loc: Location? =
        Location.RIGHT

    private var xD: Drawable? = null
    private var listener: Listener? = null

    private var l: OnTouchListener? = null
    private var f: OnFocusChangeListener? = null

    private val displayedDrawable: Drawable?
        get() = if (loc != null) compoundDrawables[loc!!.idx] else null

    enum class Location private constructor(internal val idx: Int) {
        LEFT(0), RIGHT(2)
    }

    interface Listener {
        fun didClearText()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    /**
     * null disables the icon
     */
    fun setIconLocation(loc: Location) {
        this.loc = loc
        initIcon()
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        this.l = l
    }

    override fun setOnFocusChangeListener(f: OnFocusChangeListener) {
        this.f = f
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (displayedDrawable != null) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val left = if (loc == Location.LEFT) 0 else width - paddingRight - xD!!.intrinsicWidth
            val right = if (loc == Location.LEFT) paddingLeft + xD!!.intrinsicWidth else width
            val tappedX = x in left..right && y >= 0 && y <= bottom - top
            if (tappedX) {
                if (event.action == MotionEvent.ACTION_UP) {
                    setText("")
                    if (listener != null) {
                        listener!!.didClearText()
                    }
                }
                return true
            }
        }
        return if (l != null) {
            l!!.onTouch(v, event)
        } else false
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            setClearIconVisible(text.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
        if (f != null) {
            f!!.onFocusChange(v, hasFocus)
        }
    }

    override fun onTextChanged(view: EditText, text: String) {
        if (isFocused) {
            setClearIconVisible(text.isNotEmpty())
        }
    }

    override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawables(left, top, right, bottom)
        initIcon()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        addTextChangedListener(
            TextWatcherAdapter(
                this,
                this
            )
        )
        initIcon()
        setClearIconVisible(false)
    }

    private fun initIcon() {
        xD = null
        if (loc != null) {
            xD = compoundDrawables[loc!!.idx]
        }
        if (xD == null) {
            xD = resources.getDrawable(R.drawable.ic_cross_action)
        }
        xD?.setBounds(0, 0, xD!!.intrinsicWidth, xD!!.intrinsicHeight)
        val min = paddingTop + xD!!.intrinsicHeight + paddingBottom
        if (suggestedMinimumHeight < min) {
            minimumHeight = min
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        val cd = compoundDrawables
        val displayed = displayedDrawable
        val wasVisible = displayed != null
        if (visible != wasVisible) {
            val x = if (visible) xD else null
            super.setCompoundDrawables(if (loc == Location.LEFT) x else cd[0], cd[1], if (loc == Location.RIGHT) x else cd[2],
                    cd[3])
        }
    }
}