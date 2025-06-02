package com.meticulous.homeapp.home.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.meticulous.homeapp.R
import com.meticulous.homeapp.home.domain.App


class AppDrawerRecyclerAdapter(val apps: List<App>, val onAppClick: (App) -> Unit) :
    RecyclerView.Adapter<AppDrawerRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.app_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindApp(apps[position], onAppClick)
    }

    override fun getItemCount() = apps.size

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appIcon = itemView.findViewById<ImageView>(R.id.ivAppIcon)
        private val appName = itemView.findViewById<TextView>(R.id.tvAppName)

        fun bindApp(app: App, onAppClick: (App) -> Unit) {
            appName.text = app.appName
            appIcon.setImageDrawable(app.appIcon)
            itemView.setOnClickListener {
                onAppClick(app)
            }
        }
    }
}