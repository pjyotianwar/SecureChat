package com.pjyotianwar.securechat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberImagePainter
import com.pjyotianwar.securechat.Router
import com.pjyotianwar.securechat.viewModel.PeopleViewModel

@Composable
fun ContactDetails(peopleViewModel: PeopleViewModel){
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val rec = peopleViewModel.fetchReceiver()
    val uri: String = rec.photoUrl.toString()
    val name: String = rec.displayName.toString()
    val mailId: String = rec.mail
    val lastSeen: String = "last seen recently"


    val router = Router.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
//                            peopleViewModel.onBack()
                            router.navigateUp()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }, scaffoldState = scaffoldState, modifier = Modifier.fillMaxSize(1f)
    ) {

        Column() {
            Box() {
                Image(
                    painter = rememberImagePainter(
                        data = uri.toUri(),
                        builder = { transformations(
//                        CircleCropTransformation()
                        ) }
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Column(modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart)) {
                    
                }

            }

            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = lastSeen, fontSize = 18.sp)
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Info", color = MaterialTheme.colors.secondaryVariant, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = mailId)
                Divider()
            }
        }
    }
}