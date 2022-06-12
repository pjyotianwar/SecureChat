package com.pjyotianwar.securechat.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pjyotianwar.securechat.R
import com.pjyotianwar.securechat.Routes


@Composable
fun LoginScreen(navController: NavHostController, context: ComponentActivity) {
    val emailErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            val email = remember { mutableStateOf(TextFieldValue(""))}
            val password = remember { mutableStateOf(TextFieldValue("")) }

            Spacer(modifier = Modifier.height(36.dp))

            Image(
//            imageVector = Icons.Filled.Chat,
                painter = painterResource(id = R.drawable.telegram),
                contentDescription = "null",
                modifier = Modifier
                    .size(100.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.padding(12.dp))

            Text(text = "Login",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp)

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
                Text(text = "Required Length 6 minimum", color = Color.Red)
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
                        else -> {
                            passwordErrorState.value = false
                            emailErrorState.value = false

                            Firebase.auth.signInWithEmailAndPassword(email.value.text.trim(), password.value.text.trim())
                                .addOnCompleteListener(context) { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate(Routes.Chat.route){
                                            popUpToRoute
                                        }
                                    }
                                    else {
                                        Toast.makeText(context, "Invalid mail ID or password.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
                }
            )
            {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.padding(12.dp))

            Text(text = "Or",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.padding(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (password.value.text.length<6){
                        passwordErrorState.value = true
                    }
                    else
                    navController.navigate(Routes.Register.route){
                        popUpToRoute
                    }
                }){  Text(text = "Register")}
        }
    }
}
