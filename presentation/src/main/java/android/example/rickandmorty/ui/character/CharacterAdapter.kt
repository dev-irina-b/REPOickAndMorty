package android.example.rickandmorty.ui.character

import android.example.rickandmorty.R
import android.example.domain.entities.Character
import android.example.rickandmorty.databinding.ItemCharacterBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CharacterAdapter :
    PagingDataAdapter<Character, CharacterAdapter.ViewHolder>(CharacterComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        if (character != null) {
            holder.bind(character)
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            if (character != null) {
                bundle.putInt("characterId", character.id)
            }
            Navigation.createNavigateOnClickListener(
                R.id.action_characters_to_characterDetailsFragment, bundle).onClick(it)
        }
    }

    class ViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.character = character

            Glide.with(binding.root.context)
                .load(character.image)
                .into(binding.imageView)
        }
    }

    object CharacterComparator : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}