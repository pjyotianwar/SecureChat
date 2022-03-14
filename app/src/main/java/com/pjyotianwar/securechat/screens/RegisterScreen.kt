package com.pjyotianwar.securechat.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pjyotianwar.securechat.models.Persons
import com.pjyotianwar.securechat.Routes

@Composable
fun RegisterScreen(navController: NavHostController, context: ComponentActivity) {
    val auth = Firebase.auth
    var user: FirebaseUser
    val myref= Firebase.database.getReference("user")
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf(TextFieldValue(""))}
    val nameErrorState = remember{ mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val email = remember { mutableStateOf(TextFieldValue("")) }
        val password = remember { mutableStateOf(TextFieldValue("")) }

        Image(
            imageVector = Icons.Default.Chat,
//            painter = painterResource(id = com.pjyotianwar.securechat.R.drawable.telegram),
            contentDescription = "null",
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(1f)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(text = "Register",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp)

        Spacer(modifier = Modifier.padding(12.dp))

        OutlinedTextField(
            label = {
                Text("Name")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            value = name.value,
            modifier = Modifier
                .fillMaxWidth(),
            onValueChange = {
                if (nameErrorState.value) {
                    nameErrorState.value = false
                }
                name.value = it },
            isError = nameErrorState.value
            )

        if (nameErrorState.value) {
            Text(text = "Required", color = Color.Red)
        }

        Spacer(modifier = Modifier.padding(12.dp))

        OutlinedTextField(
            label = {
                Text("Email ID")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            value = email.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                if (emailErrorState.value) {
                    emailErrorState.value = false
                }
                email.value = it
            },
            isError = emailErrorState.value)

        if (emailErrorState.value) {
            Text(text = "Required", color = Color.Red)
        }

        Spacer(modifier = Modifier.padding(8.dp))

        val passwordVisibility = remember { mutableStateOf(true) }

        OutlinedTextField(
            label = {
                Text("Password")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            value = password.value,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                if (passwordErrorState.value) {
                    passwordErrorState.value = false
                }
                password.value = it
            },
            isError = passwordErrorState.value,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(
                        imageVector =
                        if (passwordVisibility.value)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = "visibility",
                        tint = Color.Red
                    )
                }
            },
            visualTransformation =
            if (passwordVisibility.value)
                PasswordVisualTransformation()
            else
                VisualTransformation.None)

        if (passwordErrorState.value) {
            Text(text = "Required", color = Color.Red)
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                when {
                    email.value.text.isEmpty() -> {
                        emailErrorState.value = true
                    }
                    password.value.text.isEmpty() -> {
                        passwordErrorState.value = true
                    }
                    name.value.text.isEmpty() -> {
                        nameErrorState.value = true
                    }
                    else -> {

                        auth.createUserWithEmailAndPassword(email.value.text.trim(), password.value.text.trim())
                            .addOnCompleteListener(context) { task ->
                                if (task.isSuccessful) {

                                    user = auth.currentUser!!
                                    val person = Persons(user.uid, user.email.toString(), name.value.text.trim(), user.photoUrl.toString())
                                    myref.child(person.uid).setValue(person)

                                    navController.navigate(Routes.Profile.route+"/${name.value.text.trim()}"){
                                        popUpToRoute
                                    }
                                }
                                else {
                                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        )
        {
            Text(text = "Register")
        }

        Spacer(modifier = Modifier.padding(12.dp))

        Text(text = "Or",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.padding(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate(Routes.Login.route) }){  Text(text = "Login")}
    }
}
