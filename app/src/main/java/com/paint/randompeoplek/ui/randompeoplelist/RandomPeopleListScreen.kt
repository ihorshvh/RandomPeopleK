package com.paint.randompeoplek.ui.randompeoplelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.paint.randompeoplek.R
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme
import com.paint.randompeoplek.ui.theme.grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomPeopleListScreen(onItemClick: (user: User) -> Unit) {
    val viewModel = hiltViewModel<RandomPeopleListViewModel>()

    val randomPeopleListState by viewModel.randomPeopleListScreenStateFlow.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()

    val snackbarHostState = remember { SnackbarHostState() }

    RandomPeopleListScreenRoot(
        randomPeopleListScreenState = randomPeopleListState,
        snackbarHostState = snackbarHostState,
        isRefreshing = isRefreshing,
        pullRefreshState = pullRefreshState,
        onItemClick = onItemClick,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomPeopleListScreenRoot(
    randomPeopleListScreenState: RandomPeopleListScreenState,
    snackbarHostState: SnackbarHostState,
    isRefreshing: Boolean,
    pullRefreshState: PullToRefreshState,
    onItemClick: (user: User) -> Unit,
    onAction: (RandomPeopleListAction) -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = { RandomPeopleAppBar(randomPeopleListScreenState, onAction) },
        content = { innerPadding ->
            RandomPeopleListContent(
                Modifier.padding(innerPadding),
                randomPeopleListScreenState,
                isRefreshing,
                pullRefreshState,
                onItemClick,
                onAction
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomPeopleAppBar(randomPeopleListState: RandomPeopleListScreenState, onAction: (RandomPeopleListAction) -> Unit) {
    TopAppBar(
        title = {
            if (randomPeopleListState.isSearchVisible) {
                TextField(
                    value = randomPeopleListState.searchText,
                    onValueChange = { newText ->
                        onAction(RandomPeopleListAction.OnSearchTextChange(newText))
                    },
                    placeholder = {
                        Text(
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                            text = stringResource(R.string.placeholder_user_search)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onAction(RandomPeopleListAction.OnCloseSearchButtonClick)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close_img),
                                contentDescription = "Close search"
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary,
                        cursorColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.outline,
                        unfocusedTextColor = MaterialTheme.colorScheme.outline
                    )
                )
            } else {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.app_name)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            if (!randomPeopleListState.isSearchVisible) {
                IconButton(
                    onClick = { onAction.invoke(RandomPeopleListAction.OnSearchButtonClick) }
                ) {
                    Icon(painterResource(R.drawable.ic_search_img), "To search for the user")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomPeopleListContent(
    modifier: Modifier,
    randomPeopleListScreenState: RandomPeopleListScreenState,
    isRefreshing: Boolean,
    pullRefreshState: PullToRefreshState,
    onItemClick: (user: User) -> Unit,
    onAction: (RandomPeopleListAction) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onAction.invoke(RandomPeopleListAction.OnRefreshClick) },
        state = pullRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        when (randomPeopleListScreenState.randomPeopleListState) {
            is RandomPeopleListState.Initial -> RandomPeopleInitialLoading()
            is RandomPeopleListState.Success -> RandomPeopleListUsers(randomPeopleListScreenState.randomPeopleListState.users, onItemClick, onAction)
            is RandomPeopleListState.Error -> RandomPeopleListUsers(randomPeopleListScreenState.randomPeopleListState.users ?: emptyList(), onItemClick, onAction)
        }
    }
}

@Composable
fun RandomPeopleInitialLoading() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(128.dp)
                    .height(128.dp)
                    .testTag(TEST_TAG_LOADING_INDICATOR),
                strokeWidth = 10.dp
            )
            Spacer(Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.users_loading),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

val itemModifier = Modifier
    .fillMaxWidth()
    .heightIn(min = 88.dp)

@Composable
fun RandomPeopleListUsers(users: List<User>, onItemClick: (user: User) -> Unit, onAction: (RandomPeopleListAction) -> Unit) {
    if (users.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.testTag(TEST_TAG_RANDOM_PEOPLE_LIST_USERS)
        ) {
            itemsIndexed(
                items = users,
                key = { index, user: User -> user.id + index }
            )
            { index, user ->
                val testTag = if (index == users.size - 1) { TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER } else { TEST_TAG_RANDOM_PEOPLE_LIST_USER }
                RandomPeopleListItem(
                    modifier = itemModifier.testTag(testTag),
                    user = user,
                    onItemClick = { onItemClick(user) }
                )
            }
        }
    } else {
        RandomPeopleNoUsers(onAction)
    }
}

@Composable
fun RandomPeopleListItem(modifier: Modifier, user: User, onItemClick: (user: User) -> Unit) {
    Surface {
        Row(
            modifier = modifier
                .clickable { onItemClick(user) }
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
            contentDescription = "${stringResource(id = R.string.image_description)} ${user.name.fullName}",
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
        Text(
            text = user.name.shortName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = user.location,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

val dividerRowModifier = Modifier.fillMaxWidth()

@Composable
fun DividerRow() {
    Row(
        modifier = dividerRowModifier
    ) {
        HorizontalDivider(color = grey, thickness = 1.dp)
    }
}

@Composable
fun RandomPeopleNoUsers(onAction: (RandomPeopleListAction) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.label_no_users),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_update_img_layout),
                contentDescription = stringResource(id = R.string.description_no_users_image),
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clickable { onAction(RandomPeopleListAction.OnRefreshClick) }
                    .testTag(TEST_TAG_NO_USERS_IMAGE)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RandomPeopleInitialLoadingPreview() {
    RandomPeopleKTheme {
        RandomPeopleListScreenRoot(
            randomPeopleListScreenState = RandomPeopleListScreenState(randomPeopleListState = RandomPeopleListState.Initial),
            snackbarHostState = remember { SnackbarHostState() },
            isRefreshing = false,
            pullRefreshState = PullToRefreshState(),
            onItemClick = {  },
            onAction = {  }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RandomPeopleNoUsersPreview() {
    RandomPeopleKTheme {
        RandomPeopleListScreenRoot(
            randomPeopleListScreenState = RandomPeopleListScreenState(
                randomPeopleListState = RandomPeopleListState.Success(
                    users = listOf()
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            isRefreshing = false,
            pullRefreshState = PullToRefreshState(),
            onItemClick = {  },
            onAction = {  }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RandomPeopleListPreview() {
    RandomPeopleKTheme {
        RandomPeopleListScreenRoot(
            randomPeopleListScreenState = RandomPeopleListScreenState(
                isSearchVisible = true,
                searchText = "test",
                randomPeopleListState = RandomPeopleListState.Success(
                    users = listOf(
                        User(
                            id = "unique_id_1",
                            name = Name("Ire Test", "Mr. Ire Test"),
                            location = "8400 Jacksonwile road, Raintown, Greenwaland",
                            "email@gmail.com",
                            phone = "+12345678",
                            picture = Picture("", "")
                        )
                    )
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            isRefreshing = false,
            pullRefreshState = PullToRefreshState(),
            onItemClick = {  },
            onAction = {  }
        )
    }
}

@Preview
@Composable
fun RandomPeopleListItemPreview() {
    RandomPeopleKTheme {
        RandomPeopleListItem(
            modifier = itemModifier,
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

const val TEST_TAG_LOADING_INDICATOR = "loading_indicator"
const val TEST_TAG_RANDOM_PEOPLE_LIST_USERS = "random_people_list_users"
const val TEST_TAG_RANDOM_PEOPLE_LIST_USER = "random_people_list_user"
const val TEST_TAG_RANDOM_PEOPLE_LIST_LAST_USER = "random_people_list_user_last"
const val TEST_TAG_NO_USERS_IMAGE = "no_users_image"