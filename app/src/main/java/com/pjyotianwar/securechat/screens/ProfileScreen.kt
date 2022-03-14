package com.pjyotianwar.securechat.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pjyotianwar.securechat.Routes
import com.pjyotianwar.securechat.models.Persons
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController, context: Context, name: String) {

    val auth = Firebase.auth
    var user = auth.currentUser!!
    val myref= Firebase.database.getReference("user")
    val storageRef = Firebase.storage.reference.child("userImages")
    val coroutineScope = rememberCoroutineScope()

    val imageData = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent())
    {
        imageData.value = it
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        imageData.value?.let{
            Image(
                painter = rememberImagePainter(
                    data = imageData.value!!,
                    builder = {
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f),
                alignment = Alignment.Center
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                launcher.launch("image/*")
    },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Choose from gallery")
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val uref = storageRef.child("${user.uid}")
                        if (imageData.value!=null)
                        {
                            uref.putFile(imageData.value!!).addOnSuccessListener {
                                object: OnSuccessListener<Uri> {
                                    override fun onSuccess(p0: Uri?) {
                                        val downloadUri = p0
                                        val person = Persons(user.uid, user.email.toString(), name, downloadUri.toString())
                                        myref.child(user.uid).setValue(person).addOnSuccessListener {
                                            Toast.makeText(context, "Successfully set your profile photo.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        val downloadUri = task.result
//                                        val person = Persons(user.uid, user.email.toString(), name, downloadUri.toString())
//                                        myref.child(user.uid).setValue(person)
//                                    }
//                                }
                        }
                }
                navController.navigate(Routes.Chat.route)
            }) {
            Text(text = "Continue")
        }
    }
}