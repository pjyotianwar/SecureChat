package com.pjyotianwar.securechat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.pjyotianwar.securechat.Router
import com.pjyotianwar.securechat.Routes
import com.pjyotianwar.securechat.viewModel.PeopleViewModel

@Composable
fun AppDrawer(peopleViewModel: PeopleViewModel) {
    val router = Router.current
    Column {
        DrawerHeader(peopleViewModel)

        DrawerMenuItem(
            icon = Icons.Default.People,
            text = "Contacts"
        ) { router.navigate(Routes.NewChat.route) }

        DrawerMenuItem(
            icon = Icons.Default.Bookmark,
            text = "Messages to Self"
        ) {
            peopleViewModel.putNewReceiver(peopleViewModel.findSenderIndex())
            router.navigate(Routes.ChatDetail.route)
        }
        DrawerMenuItem(
            icon = Icons.Default.Person,
            text = "Profile"
        ) {
            peopleViewModel.putNewReceiver(peopleViewModel.findSenderIndex())
            router.navigate(Routes.ContactDetail.route)
        }
        Divider()
    }
}

@Composable
fun DrawerHeader(peopleViewModel: PeopleViewModel) {

    val u = peopleViewModel.fetchSender()

    Box(
        Modifier
            .fillMaxWidth()
            .height(156.dp)
            .background(color = MaterialTheme.colors.primary)
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom, modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = u.photoUrl?.toUri(),
                    builder = { transformations(CircleCropTransformation()) }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("${u.displayName}", color = Color.White, fontWeight = FontWeight.Medium)
            Text(
                "${u.mail}",
                fontWeight = FontWeight.Light,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp).fillMaxWidth()
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
//            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(28.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Medium
        )

    }
}