package app.simple.inure.adapters.batch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import app.simple.inure.R
import app.simple.inure.decorations.overscroll.RecyclerViewConstants
import app.simple.inure.decorations.overscroll.VerticalListViewHolder
import app.simple.inure.decorations.ripple.DynamicRippleRelativeLayout
import app.simple.inure.decorations.typeface.TypeFaceTextView
import app.simple.inure.decorations.views.CustomProgressBar
import app.simple.inure.glide.util.ImageLoader.loadAppIcon
import app.simple.inure.models.BatchPackageInfo

class AdapterBatchExtract(private val list: ArrayList<BatchPackageInfo>) : RecyclerView.Adapter<VerticalListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalListViewHolder {
        return when (viewType) {
            RecyclerViewConstants.TYPE_HEADER -> {
                Header(LayoutInflater.from(parent.context)
                           .inflate(R.layout.adapter_header_batch_process, parent, false))
            }
            RecyclerViewConstants.TYPE_ITEM -> {
                Holder(LayoutInflater.from(parent.context)
                           .inflate(R.layout.adapter_batch_process, parent, false))
            }
            else -> {
                throw IllegalArgumentException("there is no type that matches the type $viewType, make sure your using types correctly")
            }
        }
    }

    override fun onBindViewHolder(holder: VerticalListViewHolder, position_: Int) {
        val position = position_ - 1
        if (holder is Holder) {
            holder.icon.loadAppIcon(list[position].packageInfo.packageName)
            holder.name.text = list[position].packageInfo.applicationInfo.name
            holder.name.setStrikeThru(list[position].packageInfo.applicationInfo.enabled)

            holder.status.setText(R.string.queued)
            holder.progress.progress = 50

            holder.container.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size.plus(1)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            RecyclerViewConstants.TYPE_HEADER
        } else RecyclerViewConstants.TYPE_ITEM
    }

    inner class Header(itemView: View) : VerticalListViewHolder(itemView)

    inner class Holder(itemView: View) : VerticalListViewHolder(itemView) {
        val container: DynamicRippleRelativeLayout = itemView.findViewById(R.id.adapter_batch_process_container)
        val name: TypeFaceTextView = itemView.findViewById(R.id.adapter_batch_process_name)
        val status: TypeFaceTextView = itemView.findViewById(R.id.adapter_batch_process_status)
        val icon: ImageView = itemView.findViewById(R.id.adapter_batch_process_icon)
        val progress: CustomProgressBar = itemView.findViewById(R.id.adapter_batch_process_progress)
    }
}