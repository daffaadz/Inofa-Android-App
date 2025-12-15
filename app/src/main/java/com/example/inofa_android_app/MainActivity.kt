package com.example.inofa_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.inofa_android_app.navigation.AppNavHost
import com.example.inofa_android_app.navigation.Screen
import com.example.inofa_android_app.ui.theme.InofaAndroidAppTheme
import com.example.inofa_android_app.data.TokenStorage
import com.example.inofa_android_app.data.UserRoleStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenStorage.init(applicationContext)
        UserRoleStorage.init(applicationContext)
        
        // Check if user already logged in
        val hasToken = !TokenStorage.getToken().isNullOrEmpty()
        val startDestination = if (hasToken) Screen.Home.route else Screen.SignIn.route
        
        enableEdgeToEdge()
        setContent {
            InofaAndroidAppTheme {
                AppNavHost(
                    modifier = Modifier.fillMaxSize(),
                    startDestination = startDestination
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    InofaAndroidAppTheme {
        AppNavHost()
    }
}