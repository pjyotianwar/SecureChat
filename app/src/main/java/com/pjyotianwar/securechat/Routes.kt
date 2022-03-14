package com.pjyotianwar.securechat

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val Router = compositionLocalOf<NavHostController> { error("No active user found!") }


sealed class Routes(val route: String) {
    object Register : Routes("register")
    object Login : Routes("login")
    object NewChat : Routes("newchat")
    object Profile : Routes("profile/user")
    object Chat : Routes("chat")
    object ChatDetail : Routes("chatdetail")
    object ContactDetail : Routes("contactdetail")
}