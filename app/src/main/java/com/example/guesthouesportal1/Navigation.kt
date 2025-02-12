import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guesthouesportal1.AuthViewModel
import com.example.guesthouesportal1.ForgotPasswordScreen
import com.example.guesthouesportal1.LoginScreen
import com.example.guesthouesportal1.SignUpScreen
import com.example.guesthouesportal1.WelcomeScreen

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navigateToSignUp = { navController.navigate("signup") },
                navigateToForgotPassword = { navController.navigate("forgot_password") },
                navController
            )
        }
        composable("signup") {
            SignUpScreen( navigateToLogin = { navController.navigate("login") })
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navigateToLogin = { navController.navigate("login") })
        }

        // Add composable for WelcomeScreen with dynamic argument
        composable("welcome_screen/{name}") { backStackEntry ->
            // Get the name parameter from the navigation back stack
            val name = backStackEntry.arguments?.getString("name") ?: ""
            WelcomeScreen(name = name)
        }
    }
}
