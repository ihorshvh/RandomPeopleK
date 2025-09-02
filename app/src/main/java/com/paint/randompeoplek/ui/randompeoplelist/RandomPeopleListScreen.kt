package com.paint.randompeoplek.ui.randompeoplelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator // M3
import androidx.compose.material3.Divider // M3
import androidx.compose.material3.ExperimentalMaterial3Api // M3
import androidx.compose.material3.Icon // M3
import androidx.compose.material3.IconButton // M3
import androidx.compose.material3.MaterialTheme // M3
import androidx.compose.material3.Scaffold // M3
import androidx.compose.material3.SnackbarHost // M3
import androidx.compose.material3.SnackbarHostState // M3
import androidx.compose.material3.Surface // M3
import androidx.compose.material3.Text // M3
import androidx.compose.material3.TopAppBar // M3
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
// import androidx.compose.material.pullrefresh.PullRefreshIndicator // M2 - Commented out
// import androidx.compose.material.pullrefresh.PullRefreshState // M2 - Commented out
// import androidx.compose.material.pullrefresh.pullRefresh // M2 - Commented out
// import androidx.compose.material.pullrefresh.rememberPullRefreshState // M2 - Commented out
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // Added for M3 SnackbarHostState
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
import coil.compose.AsyncImage
import com.paint.randompeoplek.R
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import com.paint.randompeoplek.ui.theme.grey // Assuming grey is defined in your theme

@OptIn(ExperimentalMaterial3Api::class) // M3
@Composable
fun RandomPeopleListScreen(onItemClick: (user: User) -> Unit) {
    val viewModel = hiltViewModel<RandomPeopleListViewModel>()

    val onRefreshClick = { viewModel.getRandomPeopleList(RandomPeopleListViewModel.USER_QUANTITY) }

    val randomPeopleListState by viewModel.randomPeopleListStateFlow.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle() // M2 - Commented out
    val pullRefreshState = rememberPullToRefreshState()

    // For M3 compilation, using a local SnackbarHostState.
    // The ViewModel would need to be updated to provide an M3 SnackbarHostState.
    val snackbarHostState = remember { SnackbarHostState() }

    RandomPeopleListScreenRoot(
        randomPeopleListState = randomPeopleListState,
        snackbarHostState = snackbarHostState, // Pass the M3 SnackbarHostState
        isRefreshing = isRefreshing, // M2 - Commented out
        pullRefreshState = pullRefreshState, // M2 - Commented out
        onItemClick = onItemClick,
        onRefreshClick = onRefreshClick
    )
}

@OptIn(ExperimentalMaterial3Api::class) // M3
@Composable
fun RandomPeopleListScreenRoot(
    randomPeopleListState: RandomPeopleListState,
    snackbarHostState: SnackbarHostState, // M3 SnackbarHostState
    isRefreshing: Boolean, // M2 - Commented out
    pullRefreshState: PullToRefreshState, // M2 - Commented out
    onItemClick: (user: User) -> Unit,
    onRefreshClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = { RandomPeopleAppBar(onRefreshClick) },
        content = { padding ->
            RandomPeopleListContent(
                Modifier.padding(padding),
                randomPeopleListState,
                isRefreshing, // M2 - Commented out
                pullRefreshState, // M2 - Commented out
                onItemClick,
                onRefreshClick
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class) // M3 TopAppBar can be experimental
@Composable
fun RandomPeopleAppBar(onRefreshClick: () -> Unit) {
    TopAppBar( // M3 TopAppBar
        title = {
            Text(text = stringResource(id = R.string.app_name)) // M3 Text
        },
        modifier = Modifier.height(56.dp), // Height can be handled by M3 TopAppBar defaults or customized
        actions = {
            IconButton(onClick = { onRefreshClick.invoke() }) { // M3 IconButton
                Icon(painterResource(R.drawable.ic_update_img), "To refresh the user list") // M3 Icon
            }
        }
    )
}

// @OptIn(ExperimentalMaterialApi::class) // M2 - Commented out / Replaced by M3 OptIn if needed
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomPeopleListContent(
    modifier: Modifier,
    randomPeopleListState: RandomPeopleListState,
    isRefreshing: Boolean, // M2 - Commented out
    pullRefreshState: PullToRefreshState, // M2 - Commented out
    onItemClick: (user: User) -> Unit,
    onRefreshClick: () -> Unit
) {
    // M2 PullRefresh mechanism commented out for M3 migration.
    // M3 has androidx.compose.material3.pullrefresh.PullToRefreshBox for this.
    /*
    pullRefreshState?.let {
        Box(modifier.pullRefresh(it)) { // pullRefresh is M2
            when (randomPeopleListState) {
                is RandomPeopleListState.Initial -> RandomPeopleInitialLoading()
                is RandomPeopleListState.Success -> RandomPeopleListUsers(randomPeopleListState.users, onItemClick, onRefreshClick)
                is RandomPeopleListState.Error -> RandomPeopleListUsers(randomPeopleListState.users ?: emptyList(), onItemClick, onRefreshClick)
            }
            PullRefreshIndicator(isRefreshing, it, modifier.align(Alignment.TopCenter)) // M2
        }
    }
    */

    // Content without M2 PullRefresh
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefreshClick,
        state = pullRefreshState,
        modifier = modifier.fillMaxSize()) { // Added a Box to contain content, similar to original structure
        when (randomPeopleListState) {
            is RandomPeopleListState.Initial -> RandomPeopleInitialLoading()
            is RandomPeopleListState.Success -> RandomPeopleListUsers(randomPeopleListState.users, onItemClick, onRefreshClick)
            is RandomPeopleListState.Error -> RandomPeopleListUsers(randomPeopleListState.users ?: emptyList(), onItemClick, onRefreshClick)
        }
        // PullRefreshIndicator would be part of an M3 PullToRefreshBox if implemented
    }
}

@Composable
fun RandomPeopleInitialLoading() {
    // val modifier = Modifier // modifier is passed now
    Surface(modifier = Modifier.fillMaxSize()) { // M3 Surface
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator( // M3 CircularProgressIndicator
                modifier = Modifier // Removed local modifier, use passed one if needed or default
                    .width(128.dp)
                    .height(128.dp),
                strokeWidth = 10.dp
            )
            Spacer(Modifier.height(30.dp))
            Text( // M3 Text
                text = stringResource(id = R.string.users_loading),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // M3 Typography (h1 -> titleLarge)
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
fun RandomPeopleListUsers(users: List<User>, onItemClick: (user: User) -> Unit, onRefreshClick: () -> Unit) {
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
        RandomPeopleNoUsers(onRefreshClick)
    }
}

val itemModifier = Modifier
    .fillMaxWidth()
    .heightIn(min = 88.dp)

@Composable
fun RandomPeopleListItem(user: User, onItemClick: (user: User) -> Unit) {
    Surface { // M3 Surface
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

@Composable
fun ListItemImage(user: User) {
    Column(modifier = listItemImageModifier) {
        AsyncImage(
            model = user.picture.thumbnail,
            contentDescription = stringResource(id = R.string.image_description),
            placeholder = painterResource(R.drawable.ic_user_default_picture),
            error = painterResource(R.drawable.ic_user_default_picture),
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
        Text( // M3 Text
            text = user.name.shortName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold) // M3 Typography (h2 -> titleMedium)
        )
        Text( // M3 Text
            text = user.location,
            style = MaterialTheme.typography.titleSmall // M3 Typography (h3 -> titleSmall)
        )
    }
}

val dividerRowModifier = Modifier.fillMaxWidth()

@Composable
fun DividerRow() {
    Row(
        modifier = dividerRowModifier
    ) {
        Divider(color = grey, thickness = 1.dp) // M3 Divider. `grey` is custom, can be MaterialTheme.colorScheme.outline
    }
}

@Composable
fun RandomPeopleNoUsers(onRefreshClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) { // M3 Surface
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( // M3 Text
                text = stringResource(id = R.string.label_no_users),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), // M3 Typography (h1 -> titleLarge)
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_update_img_layout),
                contentDescription = stringResource(id = R.string.description_no_users_image),
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clickable { onRefreshClick.invoke() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // M3
@Preview
@Composable
fun RandomPeopleInitialLoadingPreview() {
    RandomPeopleKTheme {
        RandomPeopleListScreenRoot(
            randomPeopleListState = RandomPeopleListState.Initial,
            snackbarHostState = remember { SnackbarHostState() }, // M3
            isRefreshing = false, // M2 - Commented out
            pullRefreshState = PullToRefreshState(),
            onItemClick = {  },
            onRefreshClick = {  }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // M3
@Preview
@Composable
fun RandomPeopleNoUsersPreview() {
    RandomPeopleKTheme {
        RandomPeopleListScreenRoot(
            randomPeopleListState = RandomPeopleListState.Success(
                users = listOf()
            ),
            snackbarHostState = remember { SnackbarHostState() }, // M3
            isRefreshing = false, // M2 - Commented out
            pullRefreshState = PullToRefreshState(),
            onItemClick = {  },
            onRefreshClick = {  }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // M3
@Preview
@Composable
fun RandomPeopleListPreview() {
    RandomPeopleKTheme {
        RandomPeopleListScreenRoot(
            randomPeopleListState = RandomPeopleListState.Success(
                users = listOf(
                    User(
                        id = "unique_id_1",
                        name = Name("Ire Test", "Mr. Ire Test"),
                        location = "8400 Jacksonwile road, Raintown, Greenwaland",
                        "email@gmail.com",
                        phone = "+12345678",
                        picture = Picture("", "")
                    ) // Simplified list for brevity in this example
                )
            ),
            snackbarHostState = remember { SnackbarHostState() }, // M3
            isRefreshing = false, // M2 - Commented out
            pullRefreshState = PullToRefreshState(),
            onItemClick = {  },
            onRefreshClick = {  }
        )
    }
}

@Preview
@Composable
fun RandomPeopleListItemPreview() {
    RandomPeopleKTheme {
        RandomPeopleListItem(
            user = User(
                id = "unique_id",
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            ),
            onItemClick= {}
        )
    }
}
