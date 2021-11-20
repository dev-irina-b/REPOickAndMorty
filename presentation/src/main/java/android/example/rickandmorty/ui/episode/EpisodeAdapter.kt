package android.example.rickandmorty.ui.episode

import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.ItemEpisodeBinding
import android.example.domain.entities.Episode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class EpisodeAdapter :
    PagingDataAdapter<Episode, EpisodeAdapter.ViewHolder>(EpisodeComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = getItem(position)
        holder.bind(episode!!)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("episodeId", episode.id)
            Navigation.createNavigateOnClickListener(
                R.id.action_episodes_to_episodeDetailsFragment, bundle).onClick(it)
        }
    }

    class ViewHolder(private val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(episode: Episode) {
            binding.episode = episode
        }
    }

    object EpisodeComparator : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }
}