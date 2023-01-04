package com.paint.randompeoplek.ui.randompeoplelist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.paint.randompeoplek.R
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.randompeopleprofile.RandomPeopleProfileFragment
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import com.paint.randompeoplek.ui.theme.grey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomPeopleListFragment : Fragment() {

    private lateinit var viewModel: RandomPeopleListViewModel

    private fun onItemClick(user: User) {
        val bundle = bundleOf(RandomPeopleProfileFragment.ARG_USER to user)

        Navigation.findNavController(requireView())
            .navigate(
                R.id.action_randomPeopleListFragment_to_randomPeopleProfileFragment,
                bundle
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RandomPeopleListViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                RandomPeopleKTheme {
                    RandomPeopleListScreen(viewModel) { user -> onItemClick(user) }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
    }

    private fun setUpViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.oneTimeErrorFlow.collect { oneTimeError ->
                    handleOneTimeErrorMessageShowing(oneTimeError)
                }
            }
        }
    }

    private fun handleOneTimeErrorMessageShowing(error: ErrorEntity) {
        val oneTimeErrorMessage = getOneTimeErrorMessage(error)
        Toast.makeText(activity, oneTimeErrorMessage, Toast.LENGTH_LONG).show()
    }

    private fun getOneTimeErrorMessage(error: ErrorEntity) : String {
        return when (error) {
            is ErrorEntity.Network -> getString(R.string.error_outdated_users_loaded)
            is ErrorEntity.ServiceUnavailable -> getString(R.string.error_unknown)
            else -> getString(R.string.error_unknown)
        }
    }

}

@Composable
fun RandomPeopleListScreen(viewModel: RandomPeopleListViewModel, onItemClick: (user: User) -> Unit) {
    Scaffold(
        topBar = { AppBar { viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY) } },
        content = { padding -> Content(Modifier.padding(padding), viewModel, onItemClick) }
    )
}

@Composable
fun AppBar(onClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        modifier = Modifier.height(56.dp),
        actions = {
            IconButton(onClick = onClick) {
                Icon(painterResource(R.drawable.ic_update_img), "To refresh the user list")
            }
        }
    )
}

@Suppress("DEPRECATION") // temporary solution
@Composable
fun Content(modifier: Modifier, viewModel: RandomPeopleListViewModel, onItemClick: (user: User) -> Unit) {
    val usersResponseResource by viewModel.usersResponseFlow.collectAsState()
    
    val isRefreshing = usersResponseResource is LoadResult.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    val users = usersResponseResource.data?.response ?: emptyList()

    SwipeRefresh(
        modifier = modifier,
        state = swipeRefreshState,
        onRefresh = { viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY) }
    ) {
        if (users.isNotEmpty()) {
            LazyColumn {
                items(
                    items = users,
                    // TODO consider more specific and unique key
                    key = { user -> user.name }
                )
                { user ->
                    RandomPeopleListItem(user = user, onItemClick = { onItemClick(user) })
                }
            }
        } else {
            RandomPeopleNoUsers { viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY) }
        }
    }
}

@Composable
fun RandomPeopleListItem(user: User, onItemClick: (user: User) -> Unit) {
    Surface {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 88.dp)
                .clickable { onItemClick(user) }
        ) {
            ListItemImage(user = user)
            ListItemDescription(user = user)
        }
        DividerRow()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItemImage(user: User) {
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
    ) {
        GlideImage(
            model = user.picture.thumbnail,
            contentDescription = "Profile image",
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )
    }
}

@Composable
fun ListItemDescription(user: User) {
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        Text(
            text = user.name.shortName,
            style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = user.location,
            style = MaterialTheme.typography.h3
        )
    }
}

@Composable
fun DividerRow() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(color = grey, thickness = 1.dp)
    }
}

@Composable
fun RandomPeopleNoUsers(onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.label_no_users),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_update_img_layout),
                contentDescription = "To refresh the user list",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clickable { onClick() }
            )
        }
    }
}

@Preview
@Composable
fun RandomPeopleNoUsersPreview() {
    RandomPeopleNoUsers({})
}

@Preview
@Composable
fun AppBarPreview() {
    RandomPeopleKTheme {
        AppBar({})
    }
}

@Preview
@Composable
fun RandomPeopleListItemPreview() {
    RandomPeopleKTheme {
        RandomPeopleListItem(user = User(
            name = Name("Ire Test", "Mr. Ire Test"),
            location = "8400 Jacksonwile road, Raintown, Greenwaland",
            "email@gmail.com",
            phone = "+12345678",
            picture = Picture("", "")
        ), {})
    }
}

@Composable
fun RandomPeopleListScreen(users: List<User>) {
    Scaffold(
        topBar = { AppBar({}) },
        content = { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(items = users) { user ->
                    RandomPeopleListItem(user = user) {}
                }
            }
        }
    )
}

@Preview
@Composable
fun RandomPeopleListPreview() {
    RandomPeopleKTheme {
        RandomPeopleListScreen(listOf(
            User(
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            ),
            User(
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            ),
            User(
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            ),
            User(
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            ),
            User(
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            )))
    }
}