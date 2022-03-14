package com.pjyotianwar.securechat.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pjyotianwar.securechat.Router
import com.pjyotianwar.securechat.Routes
import com.pjyotianwar.securechat.components.PeerBubble
import com.pjyotianwar.securechat.components.UserBubble
import com.pjyotianwar.securechat.ui.theme.BottomSheetShapes
import com.pjyotianwar.securechat.viewModel.PeopleViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ChatDetailScreen(peopleViewModel: PeopleViewModel) {

    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                /* sheet content */
            }

        },
        sheetShape = BottomSheetShapes.medium,
        content = {
            Scaffold(
                topBar = { ChatDetailAppBar(peopleViewModel) },
                bottomBar = { ChatDetailBottomBar(bottomState, peopleViewModel.msg.value, peopleViewModel::changeMsg, peopleViewModel::onSendButtonClicked) },
                content = { ChatDetailBody(peopleViewModel) }
            )
        }
    )
}

@Composable
private fun ChatDetailAppBar(peopleViewModel: PeopleViewModel) {

    val router = Router.current

    var persons = peopleViewModel.fetchReceiver()

    TopAppBar(
        title = {
            Row(Modifier.padding(vertical = 4.dp).clickable {
                router.navigate(Routes.ContactDetail.route) }) {

                Image(
                    painter = rememberImagePainter(
                        data = persons.photoUrl?.toUri(),
                        builder = { transformations(CircleCropTransformation()) }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .aspectRatio(1f)
                )
                Column(Modifier.padding(start = 16.dp)) {
                    Text(text = persons.displayName.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "Last screen recently",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                peopleViewModel.onBack()
                router.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
private fun ChatDetailBody(peopleViewModel: PeopleViewModel) {
    val chats = peopleViewModel.getChat()
    val sImg = peopleViewModel.findSender().photoUrl
    val rImg = peopleViewModel.fetchReceiver().photoUrl
    LazyColumn {
        items(chats.size) { index ->
            Spacer(modifier = Modifier.height(8.dp))
            if (chats[index].sId.equals(Firebase.auth.uid)) {
                UserBubble(chats[index], sImg.toString())
            } else {
                PeerBubble(chats[index], rImg.toString())
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ChatDetailBottomBar(bottomSheetState: ModalBottomSheetState, text: TextFieldValue, onTextChanged: (TextFieldValue)-> Unit, onSendButtonClicked: ()-> Unit) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            maxLines = 4
        )

        IconButton(
            onClick =  {
                onSendButtonClicked() },
            enabled = !text.text.isEmpty(),
            content = {
                Icon(Icons.Outlined.ArrowForward,
                    tint = if(text.text.isEmpty()){MaterialTheme.colors.background}else{MaterialTheme.colors.secondaryVariant},
                    modifier = Modifier.padding(8.dp),
                    contentDescription = null)
            }
        )
    }
}