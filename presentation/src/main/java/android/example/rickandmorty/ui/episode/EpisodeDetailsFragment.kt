package android.example.rickandmorty.ui.episode

import android.example.domain.entities.Character
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import android.example.domain.entities.Episode
import android.example.rickandmorty.viewmodel.EpisodeDetailsViewModel
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodeDetailsFragment : Fragment() {

    private val viewModel: EpisodeDetailsViewModel by viewModels()

    private lateinit var binding: FragmentEpisodeDetailsBinding

    private lateinit var episode: Episode
    private var episodeId: Int? = null

    private var characters = listOf<Character>()

    private val adapter by lazy { EpisodeCharactersAdapter(characters, requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episodeId = it.getInt("episodeId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_episode_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            episodeId = it.getInt("episodeId")
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            episodeId?.let { id ->
                episode = viewModel.getEpisode(id)
                binding.apply {
                    nameTextView.text = episode.name
                    episodeTextView.text = episode.episode
                    airDateTextView.text = episode.airDate
                }
            }
            characters = viewModel.getMultipleCharacters()
            adapter.items = characters
            adapter.notifyDataSetChanged()

            binding.recycler.adapter = adapter
        }
    }

    companion object {
        fun newInstance(episodeId: Int) =
            EpisodeDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("episodeId", episodeId)
                }
            }
    }
}