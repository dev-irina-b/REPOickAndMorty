package android.example.rickandmorty.ui.location

import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.FragmentLocationsBinding
import android.example.domain.entities.Location
import android.example.rickandmorty.ui.LoadStateAdapter
import android.example.rickandmorty.util.onQueryTextChanged
import android.example.rickandmorty.viewmodel.LocationsViewModel
import android.os.Bundle
import android.view.*
import android.example.rickandmorty.util.getText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    @ExperimentalPagingApi
    private val viewModel: LocationsViewModel by viewModels()

    private var curFlowJob: Job? = null

    private lateinit var binding: FragmentLocationsBinding

    private val adapter = LocationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_locations, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        setUpViews()

        setHasOptionsMenu(true)

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        binding.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter(),
            footer = LoadStateAdapter()
        )

        curFlowJob = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loadLocations().collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.filterVisible.collectLatest {
                if (it) {
                    binding.filterConstraintLayout.visibility = View.VISIBLE
                } else
                    binding.filterConstraintLayout.visibility = View.GONE
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                val isListEmpty =
                    loadState.mediator?.refresh is LoadState.NotLoading && adapter.itemCount == 0

                binding.emptyListTextView.isVisible = isListEmpty

                binding.progressBar.isVisible =
                    loadState.mediator?.refresh is LoadState.Loading

                binding.recycler.isVisible =
                    loadState.source.refresh is LoadState.NotLoading ||
                            loadState.mediator?.refresh is LoadState.NotLoading

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    if (it.error !is CancellationException)
                    Toast.makeText(requireContext(), "${it.error.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged { searchQuery ->
            val newFlow: Flow<PagingData<Location>> =
                viewModel.loadLocations(
                name = searchQuery
            )

            curFlowJob?.cancel()
            curFlowJob = lifecycleScope.launch {
                newFlow.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    @ExperimentalPagingApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.filter -> {
                    viewModel.showFilter()
                }
            }
                return true
    }

    private fun setUpRecycler() {
        val footerAdapter = LoadStateAdapter()
        val concatAdapter = adapter.withLoadStateFooter(footer = footerAdapter)
        binding.recycler.adapter = concatAdapter
        val gridLayoutManager = GridLayoutManager(activity, 2)
        binding.recycler.layoutManager = gridLayoutManager
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    fun setUpViews() {
        binding.applyButton.setOnClickListener {
            val newFlow = viewModel.applyFilter(
                binding.nameEditText.getText,
                binding.typeEditText.getText,
                binding.dimensionEditText.getText
            )

            curFlowJob?.cancel()
            curFlowJob = lifecycleScope.launch {
                newFlow.collectLatest {
                    adapter.submitData(it)
                }
            }
            binding.nameEditText.text.clear()
            binding.typeEditText.text.clear()
            binding.dimensionEditText.text.clear()
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onRefresh() {
        val newFlow: Flow<PagingData<Location>> = viewModel.loadLocations()

        curFlowJob?.cancel()
        curFlowJob = lifecycleScope.launch {
            newFlow.collectLatest {
                adapter.submitData(it)
            }
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }
}