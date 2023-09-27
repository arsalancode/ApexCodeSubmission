package com.apex.codeassesment.ui.main.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.ui.main.MainViewModel
import com.apex.codeassesment.ui.main.MainViewState
import com.apex.codeassesment.utils.navigateDetails
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityJCompose : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val randomUser by mainViewModel.randomUser.observeAsState()
            val userListState by mainViewModel.viewStateMutableLiveData.observeAsState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                TopUserInfo(
                    user = randomUser ?: User(),
                    onNavigateToDetails = {
                        randomUser?.let { navigateDetails(it) }
                    }
                )

                RefreshRandomUser(
                    refreshUser = {
                        mainViewModel.refreshRandomUser()
                    }
                )

                LoadUserList(
                    loadUserList = {
                        mainViewModel.loadUserList(10)
                    }
                )

                when (userListState) {
                    is MainViewState.Success -> {
                        UserListScreen(
                            users = (userListState as MainViewState.Success).users,
                            onItemClick = {
                                mainViewModel.onUserListItemClicked(it)
                            }
                        )
                    }

                    is MainViewState.Loading -> {
                        LoadingScreen()
                    }

                    is MainViewState.Error -> {
                        ErrorScreen((userListState as MainViewState.Error).message)
                    }

                    else -> {}
                }

            }

        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun TopUserInfo(user: User, onNavigateToDetails: () -> Unit) {

        Column {
            Text(
                text = getString(R.string.my_random_user),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                GlideImage(
                    model = user.picture?.thumbnail ?: "",
                    contentDescription = getString(R.string.user_profile_image),
                    loading = placeholder(R.drawable.image_placeholder),
                    failure = placeholder(R.drawable.broken_image),
                    modifier = Modifier
                        .size(100.dp)
                )

                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {

                    Row {

                        Text(
                            text = getString(R.string.name),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = user.name?.first ?: "",
                            fontSize = 13.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row {

                        Text(
                            text = getString(R.string.email),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = user.email ?: "",
                            fontSize = 13.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        )

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onNavigateToDetails() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = getString(R.string.view_details))
                    }
                }
            }
        }
    }

    @Composable
    fun RefreshRandomUser(refreshUser: () -> Unit) {

        Column {

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { refreshUser() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = getString(R.string.refresh_random_user))
            }
        }
    }

    @Composable
    fun LoadUserList(loadUserList: () -> Unit) {

        Column {

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { loadUserList() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = getString(R.string.show_10_users))
            }
        }
    }

    @Composable
    fun UserListScreen(users: List<User>, onItemClick: (Int) -> Unit) {
        Column() {

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(users) { index, user ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(index) }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = user.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = colorResource(R.color.purple_500),
                modifier = Modifier.size(48.dp)
            )
        }
    }

    @Composable
    fun ErrorScreen(errorMessage: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = errorMessage,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        }
    }

}

