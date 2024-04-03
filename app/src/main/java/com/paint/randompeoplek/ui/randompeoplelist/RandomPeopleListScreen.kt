package com.paint.randompeoplek.ui.randompeoplelist

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.paint.randompeoplek.R
import com.paint.randompeoplek.model.LoadResult
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import com.paint.randompeoplek.ui.theme.grey


@Composable
fun RandomPeopleListScreen(viewModel: RandomPeopleListViewModel = hiltViewModel<RandomPeopleListViewModel>(), onItemClick: (user: User) -> Unit) {
    Scaffold(
        topBar = { RandomPeopleAppBar(viewModel) },
        content = { padding -> RandomPeopleListContent(Modifier.padding(padding), viewModel, onItemClick) }
    )
}

@Composable
fun RandomPeopleAppBar(viewModel: RandomPeopleListViewModel) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        modifier = Modifier.height(56.dp),
        actions = {
            IconButton(onClick = { viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY) }) {
                Icon(painterResource(R.drawable.ic_update_img), "To refresh the user list")
            }
        }
    )
}

@Suppress("DEPRECATION") // temporary solution
@Composable
fun RandomPeopleListContent(modifier: Modifier, viewModel: RandomPeopleListViewModel, onItemClick: (user: User) -> Unit) {
    // TODO consider to change to collectAsStateWithLifecycle()
    val usersResponseResource by viewModel.usersResponseFlow.collectAsState()

    val isRefreshing = usersResponseResource is LoadResult.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    val users = usersResponseResource.data?.response ?: emptyList()

    // TODO Confirm the swipe refresh is actual
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
            RandomPeopleNoUsers(viewModel)
        }
    }
}

val itemModifier = Modifier
    .fillMaxWidth()
    .heightIn(min = 88.dp)
@Composable
fun RandomPeopleListItem(user: User, onItemClick: (user: User) -> Unit) {
    Surface {
        Row(
            modifier = itemModifier.clickable { onItemClick(user) }
        ) {
            ListItemImage(user = user)
            ListItemDescription(user = user)
        }
        // TODO check whether divider should be added into LazyColumn directly
        DividerRow()
    }
}

val listItemImageModifier = Modifier.padding(start = 16.dp, top = 16.dp)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItemImage(user: User) {
    Column(modifier = listItemImageModifier) {
        // TODO GlideImage is supposed to work out of the box in new versions
        GlideImage(
            model = user.picture.thumbnail,
            contentDescription = "Profile image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}

val listItemDescriptionModifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
@Composable
fun ListItemDescription(user: User) {
    Column(
        modifier = listItemDescriptionModifier,
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

val dividerRowModifier = Modifier.fillMaxWidth()
@Composable
fun DividerRow() {
    Row(
        modifier = dividerRowModifier
    ) {
        Divider(color = grey, thickness = 1.dp)
    }
}

@Composable
fun RandomPeopleNoUsers(viewModel: RandomPeopleListViewModel) {
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
                    .clickable { viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY) }
            )
        }
    }
}

@Preview
@Composable
fun RandomPeopleNoUsersPreview() {
    val viewModel = hiltViewModel<RandomPeopleListViewModel>()
    RandomPeopleNoUsers(viewModel)
}

@Preview
@Composable
fun AppBarPreview() {
    val viewModel = hiltViewModel<RandomPeopleListViewModel>()
    RandomPeopleKTheme {
        RandomPeopleAppBar(viewModel)
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
    val viewModel = hiltViewModel<RandomPeopleListViewModel>()
    Scaffold(
        topBar = { RandomPeopleAppBar(viewModel) },
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
            )
        ))
    }
}