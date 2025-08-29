package com.paint.randompeoplek.ui.randompeopleprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.paint.randompeoplek.R
import com.paint.randompeoplek.domain.errorhandler.ErrorEntity
import com.paint.randompeoplek.model.Response
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

    val userResponse by viewModel.userResponseFlow.collectAsStateWithLifecycle()
    UserProfileScreenRoot(userResponse, onClick)
}

@Composable
fun UserProfileScreenRoot(userResponse: Response<User>, onClick: () -> Unit) {
    Scaffold(
        topBar = { AppBar(onClick) },
        content = { padding -> Content(Modifier.padding(padding), userResponse)}
    )
}

@Composable
fun AppBar(onClick: () -> Unit) {
    val modifier = Modifier.height(56.dp)
    TopAppBar(
        title = {
            Text(text = "")
        },
        modifier = modifier,
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
fun Content(modifier: Modifier, userResponse: Response<User>) {
    Surface(modifier = modifier.fillMaxSize()) {
        when(userResponse) {
            is Response.Initial -> ProfileLoading(modifier)
            is Response.Success -> ProfileMapping(modifier, userResponse.data)
            else -> ProfileMappingError(modifier)
        }
    }
}

@Composable
fun ProfileLoading(modifier: Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = modifier.size(160.dp)
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
    Column {
        ProfileImagePortrait(modifier, user.picture)
        ProfileName(modifier, user.name)
        ProfileLocation(modifier, user.location)
        ProfileContactInformation(modifier, user.phone, user.email)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImagePortrait(modifier: Modifier, picture: Picture) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        GlideImage(
            model = picture.medium,
            contentDescription = stringResource(id = R.string.image_description),
            modifier = modifier
                .size(160.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun ProfileName(modifier: Modifier, name: Name){
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_user_img),
                contentDescription = null,
                modifier = modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = name.fullName,
                style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.Bold),
                modifier = modifier.padding(horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun ProfileLocation(modifier: Modifier, location: String) {
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_location_img),
                contentDescription = null,
                modifier = modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = location,
                style = MaterialTheme.typography.h3,
                modifier = modifier.padding(horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun ProfileContactInformation(modifier: Modifier, phone: String, email: String) {
    Text(
        text = stringResource(R.string.label_contact_info),
        style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
        modifier = modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
    )
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_phone_img),
                contentDescription = null,
                modifier = modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = phone,
                style = MaterialTheme.typography.h3,
                modifier = modifier.padding(horizontal = 8.dp)
            )
        }
    )
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_email_img),
                contentDescription = null,
                modifier = modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = email,
                style = MaterialTheme.typography.h3,
                modifier = modifier.padding(horizontal = 8.dp)
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
    Row {
        ProfileImageLandscape(user.picture)
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp)
        ) {
            ProfileName(modifier, user.name)
            ProfileLocation(modifier, user.location)
            ProfileContactInformation(modifier, user.phone, user.email)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImageLandscape(picture: Picture) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        GlideImage(
            model = picture.medium,
            contentDescription = stringResource(id = R.string.image_description),
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
                modifier = modifier
                    .size(150.dp)
                    .padding(start = 16.dp)
            )
            Text(
                text = stringResource(R.string.error_mapping_profile),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
fun UserProfileScreenRootInitialPreview() {
    RandomPeopleKTheme {
        UserProfileScreenRoot(
            Response.Initial(),
            onClick = {  }
        )
    }
}

@Preview
@Composable
fun UserProfileScreenRootErrorPreview() {
    RandomPeopleKTheme {
        UserProfileScreenRoot(
            Response.Error(ErrorEntity.Network),
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
            Response.Success(
                data = User(
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
            Response.Success(
                data = User(
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