package android.example.rickandmorty.ui.location

import android.example.domain.entities.Character
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.FragmentLocationDetailsBinding
import android.example.domain.entities.Location
import android.example.rickandmorty.viewmodel.LocationsDetailsViewModel
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {

    @ExperimentalPagingApi
    private val viewModel: LocationsDetailsViewModel by viewModels()

    private lateinit var binding: FragmentLocationDetailsBinding

    private lateinit var location: Location
    private var locationId: Int? = null

    private var residents = listOf<Character>()

    private val adapter by lazy { LocationResidentsAdapter(residents, requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_location_details, container, false)
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            locationId = it.getInt("locationId")
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            locationId?.let { id ->
                location = viewModel.getLocation(id)
                binding.apply {
                    nameTextView.text = location.name
                    typeTextView.text = location.type
                    dimensionTextView.text = location.dimension
                }
            }
            residents = viewModel.getMultipleCharacters()
            adapter.items = residents
            adapter.notifyDataSetChanged()

            binding.recycler.adapter = adapter
        }
    }

    companion object {
        fun newInstance(locationId: Int) =
            LocationDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("locationId", locationId)
                }
            }
    }
}