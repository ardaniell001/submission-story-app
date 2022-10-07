package com.ardanil.submissionstoryapp.view.story

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.ardanil.submissionstoryapp.R
import com.ardanil.submissionstoryapp.config.DateUtils
import com.ardanil.submissionstoryapp.data.response.ListStoryItem
import com.ardanil.submissionstoryapp.databinding.ActivityDetailStoryBinding
import com.ardanil.submissionstoryapp.view.BaseActivity
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class DetailStoryActivity : BaseActivity<ActivityDetailStoryBinding>() {

	private var detailStory: ListStoryItem? = null

	override fun getViewBinding(): ActivityDetailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)

	override fun onCreate(savedInstanceState: Bundle?) {
		detailStory =
			if (Build.VERSION.SDK_INT >= 33) {
				intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
			} else {
				intent.getParcelableExtra(EXTRA_STORY)
			}
		super.onCreate(savedInstanceState)
	}

	override fun getActionToolbar(): Toolbar = binding.toolbar

	override fun configureActionBar(actionBar: ActionBar) {
		with(actionBar) {
			title = getString(R.string.detail_story_title)
			setHomeButtonEnabled(true)
			setDisplayHomeAsUpEnabled(true)
			setDisplayShowTitleEnabled(true)
		}
	}

	override fun initViews() {
		super.initViews()
		binding.apply {
			toolbar.setNavigationOnClickListener {
				onBackPressed()
			}
			tvNameInitial.text = detailStory?.name?.firstOrNull()?.toString()?.uppercase() ?: "?"
			tvName.text = detailStory?.name
			tvDescription.text = detailStory?.description
			val stringToDate = detailStory?.createdAt?.let {
				DateUtils.stringToDate(it)
			}
			tvDate.text = stringToDate?.let { DateUtils.dateToString(it) } ?: ""
			Glide.with(root.context)
				.load(detailStory?.photoUrl)
				.into(ivPhoto)
		}
	}

	companion object {
		const val EXTRA_STORY = "extra_story"
	}

}