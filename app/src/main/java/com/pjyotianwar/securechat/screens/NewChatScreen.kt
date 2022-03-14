package com.pjyotianwar.securechat.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.pjyotianwar.securechat.Router
import com.pjyotianwar.securechat.Routes
import com.pjyotianwar.securechat.models.Persons
import com.pjyotianwar.securechat.viewModel.PeopleViewModel

@Composable
fun NewChatScreen(peopleViewModel: PeopleViewModel, navController: NavHostController, context: ComponentActivity) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val personList = peopleViewModel.getPersons()

    Scaffold(
        topBar = { NewChatAppBar() },
        scaffoldState = scaffoldState
    ) {
        NewChatList(personList = personList, navController, peopleViewModel::putNewReceiver)
    }
}

@Composable
private fun NewChatAppBar() {
    val router = Router.current

    TopAppBar(
        title = {
            Column(Modifier.padding( 16.dp)) {
                Text(text = "New Chat", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        navigationIcon = {
            IconButton(onClick = { router.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
fun NewChatList( personList: List<Persons>, navController: NavHostController, onItemClicked: (Int)->Unit) {

    Log.v("in NewChatList", "${personList.size}")

    LazyColumn(modifier = Modifier.padding(2.dp)) {
        items(personList.size)
        { index ->
            Surface(
                modifier = Modifier.clickable {
                    onItemClicked(index)
                    navController.navigate(Routes.ChatDetail.route)}//+ "/${1}") }
            ) {
                NewChatItem(personList[index])
            }
        }
    }
}

@Composable
fun NewChatItem(person: Persons) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically)
    {
        Image(
            painter = rememberImagePainter(
                data = person.photoUrl?.toUri(),
                builder = { transformations(CircleCropTransformation()) }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .aspectRatio(1f)
        )
        
        Spacer(modifier = Modifier.width(8.dp))

        Text(person.displayName.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}