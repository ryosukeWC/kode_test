import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kode.R

class LoadingAdapter : RecyclerView.Adapter<LoadingAdapter.LoadingVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_worker_loading, parent, false)
        return LoadingVH(itemView)
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun onBindViewHolder(holder: LoadingVH, position: Int) {
    }

    class LoadingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}
