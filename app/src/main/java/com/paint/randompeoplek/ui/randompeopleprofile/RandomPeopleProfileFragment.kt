package com.paint.randompeoplek.ui.randompeopleprofile

import android.os.Bundle
import android.view.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.paint.randompeoplek.R
import com.paint.randompeoplek.ui.model.Name
import com.paint.randompeoplek.ui.model.Picture
import com.paint.randompeoplek.ui.model.User
import com.paint.randompeoplek.ui.theme.RandomPeopleKTheme

class RandomPeopleProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val user : User = requireArguments().getParcelable(ARG_USER)!!

        return ComposeView(requireContext()).apply {
            setContent {
                RandomPeopleKTheme {
                    UserProfileScreen(user) { findNavController().popBackStack() }
                }
            }
        }
    }

    companion object {

        const val ARG_USER = "ARG_USER"

    }

}

@Composable
fun UserProfileScreen(user : User, onClick: () -> Unit) {
    Scaffold(
        topBar = { AppBar(onClick) },
        content = { padding -> Content(Modifier.padding(padding), user)}
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
fun Content(modifier: Modifier, user : User) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            ProfileImage(user.picture)
            ProfileName(user.name)
            ProfileLocation(user.location)
            ProfileContactInformation(user.phone, user.email)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImage(picture: Picture) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp)
    ) {
        GlideImage(
            model = picture.medium,
            contentDescription = "Profile image",
            modifier = Modifier.size(160.dp).clip(CircleShape)
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
                modifier = Modifier.size(38.dp).padding(start = 16.dp)
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
                modifier = Modifier.size(38.dp).padding(start = 16.dp)
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
                modifier = Modifier.size(38.dp).padding(start = 16.dp)
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
                modifier = Modifier.size(38.dp).padding(start = 16.dp)
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

@Preview
@Composable
fun UserProfilePreview() {
    RandomPeopleKTheme {
        UserProfileScreen(
            user = User(
                name = Name("Ire Test", "Mr. Ire Test"),
                location = "8400 Jacksonwile road, Raintown, Greenwaland",
                "email@gmail.com",
                phone = "+12345678",
                picture = Picture("", "")
            ), {}
        )
    }
}