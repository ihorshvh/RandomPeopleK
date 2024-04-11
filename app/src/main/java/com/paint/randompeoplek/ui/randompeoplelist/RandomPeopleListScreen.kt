package com.paint.randompeoplek.ui.randompeoplelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RandomPeopleListContent(modifier: Modifier, viewModel: RandomPeopleListViewModel, onItemClick: (user: User) -> Unit) {
    val usersResponseResource by viewModel.usersResponseFlow.collectAsStateWithLifecycle()

    val isRefreshing = usersResponseResource is LoadResult.Loading
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY) })
    val users = usersResponseResource.data?.response ?: emptyList()

    Box(Modifier.pullRefresh(pullRefreshState)) {
        if (users.isNotEmpty()) {
            LazyColumn {
                items(
                    items = users,
                    key = { user -> user.id }
                )
                { user ->
                    RandomPeopleListItem(user = user, onItemClick = { onItemClick(user) })
                }
            }
        } else {
            RandomPeopleNoUsers(viewModel)
        }

        PullRefreshIndicator(isRefreshing, pullRefreshState, modifier.align(Alignment.TopCenter))
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
        DividerRow()
    }
}

val listItemImageModifier = Modifier.padding(start = 16.dp, top = 16.dp)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItemImage(user: User) {
    Column(modifier = listItemImageModifier) {
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
            id = "unique_id",
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
        RandomPeopleListScreen(
            listOf(
                User(
                    id = "unique_id",
                    name = Name("Ire Test", "Mr. Ire Test"),
                    location = "8400 Jacksonwile road, Raintown, Greenwaland",
                    "email@gmail.com",
                    phone = "+12345678",
                    picture = Picture("", "")
                ),
                User(
                    id = "unique_id",
                    name = Name("Ire Test", "Mr. Ire Test"),
                    location = "8400 Jacksonwile road, Raintown, Greenwaland",
                    "email@gmail.com",
                    phone = "+12345678",
                    picture = Picture("", "")
                ),
                User(
                    id = "unique_id",
                    name = Name("Ire Test", "Mr. Ire Test"),
                    location = "8400 Jacksonwile road, Raintown, Greenwaland",
                    "email@gmail.com",
                    phone = "+12345678",
                    picture = Picture("", "")
                ),
                User(
                    id = "unique_id",
                    name = Name("Ire Test", "Mr. Ire Test"),
                    location = "8400 Jacksonwile road, Raintown, Greenwaland",
                    "email@gmail.com",
                    phone = "+12345678",
                    picture = Picture("", "")
                ),
                User(
                    id = "unique_id",
                    name = Name("Ire Test", "Mr. Ire Test"),
                    location = "8400 Jacksonwile road, Raintown, Greenwaland",
                    "email@gmail.com",
                    phone = "+12345678",
                    picture = Picture("", "")
                )
            )
        )
    }
}