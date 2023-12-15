package com.vungn.backvietlibrary.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.vungn.backvietlibrary.R
import kotlin.properties.Delegates

class BookCategory : FrameLayout {
    private lateinit var textView: TextView
    private lateinit var recyclerView: RecyclerView
    private var _title: String? = null
    private var _titleColor by Delegates.notNull<Int>()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.book_category, this)
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.BookCategory, defStyle, 0
        )
        val typedValue = TypedValue()
        context.theme.resolveAttribute(
            com.google.android.material.R.attr.colorOnBackground, typedValue, true
        )

//        textView = binding.bookCategoryTitle
//        recyclerView = binding.bookCategoryList
        textView = findViewById(R.id.book_category_title)
        recyclerView = findViewById(R.id.book_category_list)
        _title = a.getString(R.styleable.BookCategory_title)
        _titleColor = a.getColor(
            R.styleable.BookCategory_textColor, resources.getColor(typedValue.resourceId, null)
        )
        setupTitle()

        a.recycle()
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun setupTitle() {
        textView.text = _title
        textView.setTextColor(_titleColor)
    }

    private fun invalidateTextPaintAndMeasurements() {
    }

    fun setAdapter(adapter: Adapter<*>) {
        recyclerView.adapter = adapter
    }

    fun setLayoutManager(layoutManager: LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    fun setDecoration(decoration: ItemDecoration) {
        recyclerView.addItemDecoration(decoration)
    }

    fun setRVHeight(height: Int) {
        recyclerView.layoutParams.height = height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom
    }
}