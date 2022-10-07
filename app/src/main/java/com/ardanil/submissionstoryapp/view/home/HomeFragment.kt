package com.ardanil.submissionstoryapp.view.home

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ardanil.submissionstoryapp.data.preference.AuthPref
import com.ardanil.submissionstoryapp.data.response.ListStoryItem
import com.ardanil.submissionstoryapp.databinding.FragmentHomeBinding
import com.ardanil.submissionstoryapp.databinding.ItemStoryBinding
import com.ardanil.submissionstoryapp.view.dataStore
import com.ardanil.submissionstoryapp.view.story.AddStoryActivity
import com.ardanil.submissionstoryapp.view.story.DetailStoryActivity
import com.ardanil.submissionstoryapp.viewmodel.AuthViewModelFactory
import com.ardanil.submissionstoryapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

	private lateinit var binding : FragmentHomeBinding
	private val storyAdapter by lazy {
		StoryAdapter(this::onStoryClicked)
	}
	private val viewModel by viewModels<HomeViewModel> {
		AuthViewModelFactory(AuthPref.getInstance(requireContext().dataStore))
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
		viewModel.getStories.observe(viewLifecycleOwner) {
			storyAdapter.submitData(viewLifecycleOwner.lifecycle, it)
			binding.rvStory.adapter = storyAdapter
		}

		storyAdapter.addLoadStateListener {
			if (it.refresh is LoadState.Loading && binding.swipeRefresh.isRefreshing) {
				binding.swipeRefresh.isRefreshing = false
			}

			if (it.append.endOfPaginationReached && storyAdapter.itemCount <= 0) {
				binding.tvNotFound.visibility = View.VISIBLE
				binding.ivNotFound.visibility = View.VISIBLE
			} else {
				binding.tvNotFound.visibility = View.GONE
				binding.ivNotFound.visibility = View.GONE
			}
		}

		binding.swipeRefresh.setOnRefreshListener {
			storyAdapter.refresh()
		}

		binding.fabAdd.setOnClickListener {
			resultLauncher.launch(Intent(requireContext(), AddStoryActivity::class.java))
		}
	}

	private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			storyAdapter.refresh()
		}
	}

	private fun onStoryClicked(item: ListStoryItem, view: ItemStoryBinding) {
		val intent = Intent(requireActivity(), DetailStoryActivity::class.java)
		intent.putExtra(DetailStoryActivity.EXTRA_STORY, item)
		val pairs = arrayOf(
			Pair<View, String>(view.ivPhoto, "transition_photo"),
			Pair<View, String>(view.tvName, "transition_name"),
			Pair<View, String>(view.tvNameInitial, "transition_name_initial"),
			Pair<View, String>(view.tvDate, "transition_date"),
			Pair<View, String>(view.tvDescription, "transition_description"),
		)
		startActivity(
			intent,
			ActivityOptions.makeSceneTransitionAnimation(requireActivity(), *pairs).toBundle()
		)
	}

}