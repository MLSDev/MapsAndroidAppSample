package com.mlsdev.mapsappsample.placesuggestions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mlsdev.mapsappsample.databinding.PlaceSuggestionsItemBinding
import java.util.*

class PlacesAdapter(
        private val onItemClickListener: (place:String) -> Unit
) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {
    private var places: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaceSuggestionsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewModel = PlaceViewModel(onItemClickListener)
        binding.viewModel = viewModel
        return ViewHolder(binding.root, viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewModel.placeFullText.set(places[position])
    }

    override fun getItemCount(): Int {
        return places.size
    }

    inner class ViewHolder(itemView: View, val viewModel: PlaceViewModel) : RecyclerView.ViewHolder(itemView)

    fun setData(places: MutableList<String>) {
        this.places = places
        notifyDataSetChanged()
    }

    fun clearData() {
        places.clear()
        notifyDataSetChanged()
    }
}
