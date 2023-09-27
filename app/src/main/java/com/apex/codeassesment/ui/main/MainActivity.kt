package com.apex.codeassesment.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.apex.codeassesment.R
import com.apex.codeassesment.databinding.ActivityMainBinding
import com.apex.codeassesment.ui.main.MainViewState.Error
import com.apex.codeassesment.ui.main.MainViewState.Loading
import com.apex.codeassesment.ui.main.MainViewState.Success
import com.apex.codeassesment.ui.main.compose.MainActivityJCompose
import com.apex.codeassesment.utils.navigateDetails
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter(mutableListOf(), this)

        mainViewModel.randomUser.observe(this) { user ->
            user?.let {
                binding.apply {
                    Glide.with(this@MainActivity).load(it.picture?.thumbnail).into(mainImage)
                    mainName.text = getString(R.string.main_name, it.name?.first, it.name?.last)
                    mainEmail.text = "${it.email}"
                    mainUserList.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        addItemDecoration(
                            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                                ContextCompat.getDrawable(context, R.drawable.divider)
                                    ?.let { it1 -> setDrawable(it1) }
                            }
                        )

                        adapter = userAdapter
                    }
                }
            }
        }

        binding.mainSceenDetailsButton.setOnClickListener {
            mainViewModel.randomUser.value?.let { navigateDetails(it) }
        }

        binding.mainRefreshButton.setOnClickListener {
            mainViewModel.refreshRandomUser()
        }

        binding.mainUserListButton.setOnClickListener {
            mainViewModel.loadUserList(10)
        }

        mainViewModel.viewStateMutableLiveData.observe(this) { mainViewState ->
            when(mainViewState){
                is Loading -> binding.userListLoading.visibility = View.VISIBLE
                is Success -> {
                    binding.userListLoading.visibility = View.GONE
                    userAdapter.updateData(mainViewState.users)
                }
                is Error -> {
                    binding.userListLoading.visibility = View.GONE
                    Toast.makeText(this, mainViewState.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.openComposeView.setOnClickListener {
            val intent = Intent(this, MainActivityJCompose::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(position: Int) {
        mainViewModel.onUserListItemClicked(position)
    }
}
