package android.example.rickandmorty.ui.location

import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.ItemLocationBinding
import android.example.domain.entities.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter :
    PagingDataAdapter<Location, LocationAdapter.ViewHolder>(LocationComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location!!)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("locationId", location.id)
            Navigation.createNavigateOnClickListener(
                R.id.action_locations_to_locationDetailFragment, bundle).onClick(it)
        }
    }

    class ViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location) {
            binding.location = location
        }
    }

    object LocationComparator : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }
}