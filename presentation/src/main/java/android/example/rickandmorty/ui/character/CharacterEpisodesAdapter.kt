package android.example.rickandmorty.ui.character

import android.content.Context
import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.ItemEpisodeBinding
import android.example.domain.entities.Episode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class CharacterEpisodesAdapter(var items: List<Episode>, val context: Context):
    RecyclerView.Adapter<CharacterEpisodesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = items[position]
        holder.bind(episode)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("episodeId", episode.id)
            Navigation.createNavigateOnClickListener(
                R.id.action_characterDetailsFragment_to_episodeDetailsFragment, bundle).onClick(it)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ItemEpisodeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode) {
            binding.episode = episode
        }
    }
}