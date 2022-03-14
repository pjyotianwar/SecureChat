package com.pjyotianwar.securechat

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.pjyotianwar.securechat.ui.theme.TelegramCloneTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pjyotianwar.securechat.screens.*
import com.pjyotianwar.securechat.viewModel.PeopleViewModel

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    val peopleViewModel by viewModels<PeopleViewModel>()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TelegramCloneTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    CompositionLocalProvider(Router provides navController) {
                        MainScreen(this, peopleViewModel)
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(context: ComponentActivity, peopleViewModel: PeopleViewModel) {

    val navController = Router.current


    NavHost(navController = navController,
        startDestination = if (Firebase.auth.currentUser!=null){
            Routes.Chat.route
    }
    else{
            Routes.Login.route
    }) {

        composable(Routes.Chat.route) {
            ChatScreen(navController = navController, peopleViewModel)
        }

        composable(Routes.Login.route) {
            LoginScreen(navController = navController, context)
        }

        composable(Routes.NewChat.route){
            NewChatScreen(peopleViewModel = peopleViewModel, navController = navController, context)
        }

        composable(Routes.Register.route) {
            RegisterScreen(navController = navController, context)
        }

        composable(Routes.Profile.route+"/{user}") {
            val name = it.arguments?.getString("user")
            if (name != null) {
                ProfileScreen(navController = navController, context, name)
            }
        }
        
        composable(Routes.ContactDetail.route){
            ContactDetails(peopleViewModel)
        }

        composable(Routes.ChatDetail.route ) {
//            val from = it.arguments?.getInt("from")           + "/{from}"
//             val i =  navController.previousBackStackEntry?.arguments.getParcelable<Chats>("chat")
//            if (from != null) {
                ChatDetailScreen(peopleViewModel)
//            }
        }
    }
}



@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true, name = "Light Mode")
@Preview(name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun DefaultPreview() {
    TelegramCloneTheme {
//        MainScreen(LocalContext.current)
    }
}