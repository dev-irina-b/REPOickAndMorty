package android.example.rickandmorty.ui.character

import android.example.domain.entities.Character
import android.example.rickandmorty.R
import android.example.rickandmorty.databinding.FragmentCharactersBinding
import android.example.rickandmorty.ui.LoadStateAdapter
import android.example.rickandmorty.util.getText
import android.example.rickandmorty.util.onQueryTextChanged
import android.example.rickandmorty.viewmodel.CharactersViewModel
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
class CharactersFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    @ExperimentalPagingApi
    val viewModel: CharactersViewModel by viewModels()

    private var curFlowJob: Job? = null

    private lateinit var binding: FragmentCharactersBinding

    private val adapter = CharacterAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_characters, container, false)
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

        curFlowJob = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loadCharacters()
                .collectLatest { pagingData ->
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
            val newFlow: Flow<PagingData<Character>> =
                viewModel.loadCharacters(name = searchQuery)

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
            val newFlow: Flow<PagingData<Character>> = viewModel.applyFilter(
                binding.nameEditText.getText,
                binding.statusSpinner.selectedItem.toString(),
                binding.speciesEditText.getText,
                binding.typeEditText.getText,
                binding.genderSpinner.selectedItem.toString()
            )

            curFlowJob?.cancel()
            curFlowJob = lifecycleScope.launch {
                newFlow.collectLatest {
                    adapter.submitData(it)
                }
            }

            binding.nameEditText.text.clear()
            binding.statusSpinner.setSelection(
                resources.getStringArray(R.array.status).indexOf("All"))
            binding.speciesEditText.text.clear()
            binding.typeEditText.text.clear()
            binding.genderSpinner.setSelection(
                resources.getStringArray(R.array.gender).indexOf("All"))
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    override fun onRefresh() {
        val newFlow: Flow<PagingData<Character>> = viewModel.loadCharacters()

        curFlowJob?.cancel()
        curFlowJob = lifecycleScope.launch {
            newFlow.collectLatest {
                adapter.submitData(it)
            }
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }
}