package com.paint.randompeoplek.ui.randompeopleprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.paint.randompeoplek.R
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.screen.ScreenInfo
import com.paint.randompeoplek.ui.screen.rememberScreenInfo
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme

@Composable
fun UserProfileScreen(userId: String, onClick: () -> Unit) {
    val viewModel: RandomPeopleProfileViewModel = hiltViewModel<RandomPeopleProfileViewModel>()

    LaunchedEffect(true) {
        viewModel.getUserById(userId)
    }

    val randomPeopleProfileState by viewModel.randomPeopleProfileStateFlow.collectAsStateWithLifecycle()
    UserProfileScreenRoot(randomPeopleProfileState, onClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreenRoot(randomPeopleProfileState: RandomPeopleProfileState, onClick: () -> Unit) {
    Scaffold(
        topBar = { AppBar(onClick) },
        content = { paddingValues ->
            Content(
                Modifier.padding(paddingValues),
                randomPeopleProfileState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_img),
                    contentDescription = stringResource(id = R.string.description_navigation_image)
                )
            }
        }
    )
}

@Composable
fun Content(modifier: Modifier, randomPeopleProfileState: RandomPeopleProfileState) {
    Surface(modifier = modifier.fillMaxSize()) {
        when(randomPeopleProfileState) {
            is RandomPeopleProfileState.Initial -> ProfileLoading(modifier = Modifier.fillMaxSize())
            is RandomPeopleProfileState.Success -> ProfileMapping(modifier = Modifier.fillMaxSize(), randomPeopleProfileState.user)
            else -> ProfileMappingError(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun ProfileLoading(modifier: Modifier) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(160.dp)
            )
        }
    }
}

@Composable
fun ProfileMapping(modifier: Modifier, user: User?) {
    if (user != null) {
        ProfileMappingScreen(modifier, user)
    } else {
        ProfileMappingError(modifier)
    }
}

@Composable
fun ProfileMappingScreen(modifier: Modifier, user: User) {
    val screenInfo = rememberScreenInfo()
    if (screenInfo.screenOrientation == ScreenInfo.ScreenOrientation.Portrait) {
        ProfileMappingScreenPortrait(modifier, user)
    } else {
        ProfileMappingScreenLandscape(modifier, user)
    }
}

@Composable
fun ProfileMappingScreenPortrait(modifier: Modifier, user: User) {
    Column(modifier = modifier) {
        ProfileImagePortrait(user.picture)
        ProfileName(user.name)
        ProfileLocation(user.location)
        ProfileContactInformation(user.phone, user.email)
    }
}

@Composable
fun ProfileImagePortrait(picture: Picture) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        AsyncImage(
            model = picture.medium,
            contentDescription = stringResource(id = R.string.image_description),
            placeholder = painterResource(R.drawable.ic_user_default_picture),
            error = painterResource(R.drawable.ic_user_default_picture),
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun ProfileName(name: Name){
    TextWithTheImageToTheLeft(
        image = {
            Image(
                painter = painterResource(R.drawable.ic_user_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        text = {
            Text(
                text = name.fullName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun ProfileLocation(location: String) {
    TextWithTheImageToTheLeft(
        image = {
            Image(
                painter = painterResource(R.drawable.ic_location_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        text = {
            Text(
                text = location,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun ProfileContactInformation(phone: String, email: String) {
    Text(
        text = stringResource(R.string.label_contact_info),
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
    )
    TextWithTheImageToTheLeft(
        image = {
            Image(
                painter = painterResource(R.drawable.ic_phone_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        text = {
            Text(
                text = phone,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    )
    TextWithTheImageToTheLeft(
        image = {
            Image(
                painter = painterResource(R.drawable.ic_email_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        text = {
            Text(
                text = email,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun TextWithTheImageToTheLeft(image: @Composable () -> Unit, text: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        image()
        text()
    }
}

@Composable
fun ProfileMappingScreenLandscape(modifier: Modifier, user: User) {
    Row(modifier = modifier) {
        ProfileImageLandscape(user.picture)
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            ProfileName(user.name)
            ProfileLocation(user.location)
            ProfileContactInformation(user.phone, user.email)
        }
    }
}

@Composable
fun ProfileImageLandscape(picture: Picture) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        AsyncImage(
            model = picture.medium,
            contentDescription = stringResource(id = R.string.image_description),
            placeholder = painterResource(R.drawable.ic_user_default_picture),
            error = painterResource(R.drawable.ic_user_default_picture),
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun ProfileMappingError(modifier: Modifier) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_profile_load_error),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(start = 16.dp)
            )
            Text(
                text = stringResource(R.string.error_mapping_profile),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
fun UserProfileScreenRootInitialPreview() {
    RandomPeopleKTheme {
        UserProfileScreenRoot(
            RandomPeopleProfileState.Initial,
            onClick = {  }
        )
    }
}

@Preview
@Composable
fun UserProfileScreenRootErrorPreview() {
    RandomPeopleKTheme {
        UserProfileScreenRoot(
            RandomPeopleProfileState.Error(),
            onClick = {  }
        )
    }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL_7
)
@Composable
fun UserProfileScreenRootSuccessPortraitPreview() {
    RandomPeopleKTheme {
        UserProfileScreenRoot(
            RandomPeopleProfileState.Success(
                user = User(
                    id = "unique_id",
                    name = Name("Ire Test", "Mr. Ire Test"),
                    location = "8400 Jacksonwile road, Raintown, Greenwaland",
                    "email@gmail.com",
                    phone = "+12345678",
                    picture = Picture("", "")
                )
            ),
            onClick = {  }
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:parent=pixel_7,orientation=landscape"
)
@Composable
fun UserProfileScreenRootSuccessLandscapePreview() {
    RandomPeopleKTheme {
        UserProfileScreenRoot(
            RandomPeopleProfileState.Success(
                user = User(
                    id = "unique_id",
                    name = Name("Ire Test", "Mr. Ire Test"),
                    location = "8400 Jacksonwile road, Raintown, Greenwaland",
                    "email@gmail.com",
                    phone = "+12345678",
                    picture = Picture("", "")
                )
            ),
            onClick = {  }
        )
    }
}