package com.example.rztest.activity

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rztest.R
import com.example.rztest.adapter.RvAdapter
import com.example.rztest.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private val layoutManager = GridLayoutManager(this, 2)
    private val mViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var byteArray: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        byteArray = getByteArrayFromFile(getString(R.string.image_name))

        //        EncoderUtil.toImage("", "", applicationContext)

        rv_main.layoutManager = layoutManager

        val imageWidth = getScreenWidthWithPadding()
        val imageHeight = (imageWidth * 1.25).toInt()

        mViewModel.getPopular(imageWidth, imageHeight, byteArray)

        rv_main.adapter = RvAdapter(
            mutableListOf(),
            imageWidth,
            imageHeight
        )

        et_search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    (rv_main.adapter as RvAdapter).cleanView()
                    mViewModel.cleanPaginationIndex()
                    mViewModel.searchWithQuery(
                        et_search.text.toString(),
                        imageWidth,
                        imageHeight,
                        byteArray
                    )
                    hideKeyboardFrom(context = this@MainActivity, view = iv_search)
                    return true
                }
                return false
            }
        })

        iv_search.setOnClickListener {
            (rv_main.adapter as RvAdapter).cleanView()
            mViewModel.cleanPaginationIndex()
            mViewModel.searchWithQuery(
                et_search.text.toString(),
                imageWidth,
                imageHeight,
                byteArray
            )
            hideKeyboardFrom(context = this, iv_search)
        }

        rv_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    val visibleItemCount: Int = layoutManager.childCount
                    val totalItemCount: Int = layoutManager.itemCount
                    val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                    mViewModel.scrollViewer(
                        visibleItemCount,
                        firstVisibleItemPosition,
                        totalItemCount,
                        et_search.text.toString(),
                        imageWidth,
                        imageHeight,
                        byteArray
                    )
                }
            }
        })

        mViewModel.getData().observe(this) { dataResponse ->
            dataResponse?.let {
                (rv_main.adapter as RvAdapter).updateList(it.toMutableList())
                isAdapterFilledChecker()
            }
        }

        mViewModel.getShowingProgress().observe(this) { bool ->
            updateProgressViewState(bool)
        }

        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    private fun getScreenWidthWithPadding(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val width: Int = size.x
        return width / 2
    }

    private fun getByteArrayFromFile(fileName: String): ByteArray? {
        var buffer: ByteArray? = null
        val inpStr: InputStream
        try {
            inpStr = assets.open(fileName)
            val size = inpStr.available()
            buffer = ByteArray(size)
            inpStr.read(buffer)
            inpStr.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return buffer
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isAdapterFilledChecker() {
        if ((rv_main.adapter as RvAdapter).itemCount == 0) {
            tv_no_result.visibility = View.VISIBLE
        } else {
            tv_no_result.visibility = View.GONE
        }
    }

    private fun updateProgressViewState(boolean: Boolean) {
        if (boolean) {
            pv_view.visibility = View.VISIBLE
            mViewModel.updateIsLoadingStatus(true)
        } else {
            pv_view.visibility = View.GONE
            mViewModel.updateIsLoadingStatus(false)
        }
    }
}