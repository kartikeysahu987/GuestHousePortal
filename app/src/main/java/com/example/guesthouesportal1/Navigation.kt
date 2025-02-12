import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guesthouesportal1.AuthViewModel
import com.example.guesthouesportal1.ForgotPasswordScreen
import com.example.guesthouesportal1.LoginScreen
import com.example.guesthouesportal1.SignUpScreen

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navigateToSignUp = { navController.navigate("signup") },
                navigateToForgotPassword = { navController.navigate("forgot_password") }
            )
        }
        composable("signup") {
            SignUpScreen( navigateToLogin = { navController.navigate("login") })
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navigateToLogin = { navController.navigate("login") })
        }
    }
}
