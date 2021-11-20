package android.example.rickandmorty.ui.episode

import android.content.Context
import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.ItemCharacterBinding
import android.os.Bundle
import android.example.domain.entities.Character
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class EpisodeCharactersAdapter(var items: List<Character>, val context: Context):
    RecyclerView.Adapter<EpisodeCharactersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = items[position]
        holder.bind(character)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("characterId", character.id)
            Navigation.createNavigateOnClickListener(
                R.id.action_episodeDetailsFragment_to_characterDetailsFragment, bundle).onClick(it)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ItemCharacterBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.character = character
        }
    }
}