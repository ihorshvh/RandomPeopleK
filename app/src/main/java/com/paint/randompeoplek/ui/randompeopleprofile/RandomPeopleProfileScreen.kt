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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.paint.randompeoplek.R
import com.paint.randompeoplek.model.Response
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User

@Composable
fun UserProfileScreen(viewModel: RandomPeopleProfileViewModel = hiltViewModel<RandomPeopleProfileViewModel>(), userId: String, onClick: () -> Unit) {
    Scaffold(
        topBar = { AppBar(onClick) },
        content = { padding -> Content(Modifier.padding(padding), userId, viewModel)}
    )
}

@Composable
fun AppBar(onClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "")
        },
        modifier = Modifier.height(56.dp),
        navigationIcon = {
            IconButton(
                onClick = onClick,
            ) {
                Icon(painter = painterResource(R.drawable.ic_back_img), contentDescription = "Navigation icon")
            }
        }
    )
}

@Composable
fun Content(modifier: Modifier, userId: String, viewModel: RandomPeopleProfileViewModel) {
    Surface(modifier = modifier.fillMaxSize()) {
        val userResponse by viewModel.userResponseFlow.collectAsStateWithLifecycle()

        LaunchedEffect(true) {
            viewModel.getUserById(userId)
        }

        when(userResponse) {
            is Response.Initial -> ProfileLoading()
            is Response.Success -> ProfileMapping(userResponse.data)
            else -> ProfileMappingError()
        }
    }
}

@Composable
fun ProfileLoading() {
    Surface(modifier = Modifier.fillMaxSize()) {
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
fun ProfileMapping(user: User?) {
    if (user != null) {
        Column {
            ProfileImage(user.picture)
            ProfileName(user.name)
            ProfileLocation(user.location)
            ProfileContactInformation(user.phone, user.email)
        }
    } else {
        ProfileMappingError()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImage(picture: Picture) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        GlideImage(
            model = picture.medium,
            contentDescription = "Profile image",
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun ProfileName(name: Name){
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_user_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = name.fullName,
                style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun ProfileLocation(location: String) {
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_location_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = location,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    )
}

@Composable
fun ProfileContactInformation(phone: String, email: String) {
    Text(
        text = stringResource(R.string.label_contact_info),
        style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
    )
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_phone_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = phone,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    )
    TextWithTheImageToTheLeft(
        {
            Image(
                painter = painterResource(R.drawable.ic_email_img),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .padding(start = 16.dp)
            )
        },
        {
            Text(
                text = email,
                style = MaterialTheme.typography.h3,
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
fun ProfileMappingError() {
    val modifier = Modifier.fillMaxSize()

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
                style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
fun UserLoadingPreview() {
    ProfileLoading()
}

@Preview
@Composable
fun ProfileMappingErrorPreview() {
    ProfileMappingError()
}