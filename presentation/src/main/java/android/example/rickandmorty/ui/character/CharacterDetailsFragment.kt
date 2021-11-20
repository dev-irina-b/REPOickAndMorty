package android.example.rickandmorty.ui.character

import android.example.domain.entities.Character
import android.example.domain.entities.Episode
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.FragmentCharacterDetailsBinding
import android.example.rickandmorty.viewmodel.CharactersDetailsViewModel
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {

    @ExperimentalPagingApi
    private val viewModel: CharactersDetailsViewModel by viewModels()

    private lateinit var binding: FragmentCharacterDetailsBinding

    private lateinit var character: Character
    private var episodes = listOf<Episode>()

    private val adapter by lazy { CharacterEpisodesAdapter(episodes, requireContext()) }

    private var characterId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_character_details, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            characterId = it.getInt("characterId")
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            characterId?.let { id ->
                character = viewModel.getCharacter(id)
                        binding.apply {
                            nameTextView.text = character.name
                            statusTextView.text = character.status
                            speciesTextView.text = character.species
                            typeTextView.text = character.type
                            genderTextView.text = character.gender
                            originTextView.text = character.origin.name
                            locationTextView.text = character.location.name

                            binding.originArrowImageView.visibility =
                                if (character.origin.name == "unknown") View.GONE else View.VISIBLE

                            binding.locationArrowImageView.visibility =
                                if (character.location.name == "unknown") View.GONE else View.VISIBLE
                        }
                        loadImage()
                    }
            episodes = viewModel.getMultipleEpisodes()
            adapter.items = episodes
            adapter.notifyDataSetChanged()

            binding.recycler.adapter = adapter
        }
        setUpViews()
    }

    private fun setUpViews() {
        binding.originCardView.setOnClickListener {
            val bundle = Bundle()
            val originId = character.origin.url.substringAfter("location/")
            if (originId.isNotBlank()) {
                bundle.putInt("locationId", originId.toInt())
                Navigation.createNavigateOnClickListener(
                    R.id.action_characterDetailsFragment_to_locationDetailsFragment, bundle
                ).onClick(it)
            }
        }

        binding.locationCardView.setOnClickListener {
            val bundle = Bundle()
            val locationId = character.location.url.substringAfter("location/")
            if (locationId.isNotBlank()) {
                bundle.putInt("locationId", locationId.toInt())
                Navigation.createNavigateOnClickListener(
                    R.id.action_characterDetailsFragment_to_locationDetailsFragment, bundle
                ).onClick(it)
            }
        }
    }

    private fun loadImage() {
        Glide.with(binding.root.context)
            .load(character.image)
            .into(binding.imageView)
    }

    companion object {
        fun newInstance(characterId: Int) =
            CharacterDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("characterId", characterId)
                }
            }
    }
}