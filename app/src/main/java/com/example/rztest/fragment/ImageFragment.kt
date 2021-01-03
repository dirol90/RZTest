package com.example.rztest.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rztest.R
import com.example.rztest.model.UnsplashPhotoModel
import com.example.rztest.utils.DownloadUtil
import kotlinx.android.synthetic.main.fragment_image.*


class ImageFragment(var unsplashPhotoModel: UnsplashPhotoModel) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide
            .with(view.context)
            .load(unsplashPhotoModel.urls.regular)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv_photo)

        if (unsplashPhotoModel.altDescription != null) tv_header.text =
            unsplashPhotoModel.altDescription.capitalize() else tv_header.text = ""
        if (unsplashPhotoModel.description != null) tv_description.text =
            unsplashPhotoModel.description.toString().capitalize() else tv_description.text = ""
        if (unsplashPhotoModel.likes != null) tv_likes?.text = unsplashPhotoModel.likes.toString()
        if (unsplashPhotoModel.views != null) tv_views?.text = unsplashPhotoModel.views.toString()
        if (unsplashPhotoModel.downloads != null) tv_downloads?.text =
            unsplashPhotoModel.downloads.toString()

        btn_download.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    view.context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    view.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    view.context as Activity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
            unsplashPhotoModel.links.download.let {
                DownloadUtil.downloadResourceTask(
                    view.context,
                    Uri.parse(it),
                    unsplashPhotoModel.id
                )
            }

            Toast.makeText(
                context,
                getString(R.string.toast_text),
                Toast.LENGTH_LONG
            ).show()
        }

        btn_share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = "${unsplashPhotoModel.altDescription}\n${unsplashPhotoModel.urls.full}"
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                unsplashPhotoModel.links.download
            )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, getString(R.string.shsare_txt)))
        }

        ib_back.setOnClickListener {
            activity?.onBackPressed()
        }

        fl_view.setOnClickListener { }
    }
}