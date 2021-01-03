package com.example.rztest.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.rztest.R
import com.example.rztest.fragment.ImageFragment
import com.example.rztest.model.UnsplashPhotoModel


class RvAdapter(
    private var values: MutableList<UnsplashPhotoModel?>?,
    private var cardWidth: Int,
    private var cardHeight: Int
) :
    RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun getItemCount(): Int {
        var returnValue = values?.size
        if (returnValue == null) {
            returnValue = 0
        }
        return returnValue
    }

    fun updateList(newValues: List<UnsplashPhotoModel?>) {
        val lastIndex = values?.size
        values?.addAll(newValues)
        lastIndex?.let { this.notifyItemRangeChanged(it - 1, newValues.size) }
    }

    fun cleanView() {
        values?.clear()
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_card, parent, false)
        this.context = parent.context
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        values?.let {
            it[position]?.let { it1 ->
                if (it1.likes != null) holder.tvLikes?.text = it1.likes.toString()
                if (it1.views != null) holder.tvViews?.text = it1.views.toString()
                if (it1.downloads != null) holder.tvDownloads?.text = it1.downloads.toString()

                Glide
                    .with(context!!)
                    .load(it1.urls.regular)
                    .apply(RequestOptions().override(cardWidth, cardHeight))
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .override(cardWidth, cardHeight)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivImage!!)

                holder.cvView?.setOnClickListener {
                    val manager: FragmentManager =
                        (context as AppCompatActivity).supportFragmentManager
                    val transaction: FragmentTransaction = manager.beginTransaction()
                    val fragment = ImageFragment(it1)
                    transaction.add(
                        R.id.fragment_container_view,
                        fragment,
                        ImageFragment::class.java.simpleName
                    )
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLikes: TextView? = null
        var tvViews: TextView? = null
        var tvDownloads: TextView? = null
        var ivImage: ImageView? = null
        var cvView: CardView? = null

        init {
            tvLikes = itemView.findViewById(R.id.tv_likes)
            tvViews = itemView.findViewById(R.id.tv_views)
            tvDownloads = itemView.findViewById(R.id.tv_downloads)
            ivImage = itemView.findViewById(R.id.iv_image)
            cvView = itemView.findViewById(R.id.cv_view)
        }
    }
}