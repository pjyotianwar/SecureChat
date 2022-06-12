package com.pjyotianwar.securechat.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pjyotianwar.securechat.Routes
import com.pjyotianwar.securechat.components.AppDrawer
import com.pjyotianwar.securechat.convertLongToTime
import com.pjyotianwar.securechat.decrypt
import com.pjyotianwar.securechat.models.LastChat
import com.pjyotianwar.securechat.viewModel.PeopleViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(navController: NavHostController, peopleViewModel: PeopleViewModel) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val lastList = peopleViewModel.getLastChat()

//    Log.v("ChatScreen", "${lastList.size}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Secure Chat") },
                actions = {
                    IconButton(onClick = {
                        Firebase.auth.signOut()
                        navController.navigate(Routes.Login.route){
                            popUpToRoute
                        }
                        peopleViewModel.signOut()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = null)
                    }
                          },
                navigationIcon = { IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                } }
            )
        },
        drawerContent = { AppDrawer(peopleViewModel) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.NewChat.route) }) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
            }
        },
        scaffoldState = scaffoldState
    ) {
        ChatList(navController, lastList = lastList, peopleViewModel::putLastReceiver)
    }
}

@Composable
fun ChatList(navController: NavHostController, lastList: List<LastChat>, onItemClicked: (Int)-> Unit) {
    Log.v("ChatList", "${lastList.size}")
    LazyColumn {
        items(lastList.size)
        { index ->
            ChatItem(lastList[index],
                onClick = {
                    onItemClicked(index)
                    navController.navigate(Routes.ChatDetail.route)}// + "/${0}") }
            )
        }
    }
}

@Composable
fun ChatItem(chat: LastChat, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() })
    {
        Image(
        painter = rememberImagePainter(
            data = chat.peer.photoUrl?.toUri(),
            builder = {
                transformations(CircleCropTransformation())
            }),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .aspectRatio(1f)
        )

        Column(
            Modifier
                .padding(horizontal = 14.dp)
                .weight(6f)) {
            Text(
                chat.peer.displayName.toString(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                decrypt(chat.lastMessage.toString()),
                maxLines = 1,
                fontSize = 16.sp,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(Modifier.weight(2f)) {
            Text(
                convertLongToTime(chat.time!!.toLong()),
                fontWeight = FontWeight.Light,
                fontSize = 13.sp, textAlign = TextAlign.Right)
        }
    }
}
