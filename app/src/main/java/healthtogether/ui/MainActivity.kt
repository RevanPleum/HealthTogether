package healthtogether.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import healthtogether.components.AppColor
import healthtogether.components.AuthScreen
import healthtogether.components.SetStatusBarColor
import healthtogether.ui.fogetPassword.ForgetPassword
import healthtogether.ui.fogetPassword.SendPasswordPage
import healthtogether.ui.fogetPassword.forgetPasswordViewModel.ForgetPasswordViewModel
import healthtogether.ui.login.FaceScanView
import healthtogether.ui.login.LoginView
import healthtogether.ui.login.loginmodel.LoginPageViewModel
import healthtogether.ui.register.RegisterCompletedPage
import healthtogether.ui.register.RegisterConfirmOTPPage
import healthtogether.ui.register.RegisterMemberFormView
import healthtogether.ui.register.RegisterTrainerFormView
import healthtogether.ui.register.RegisterView
import healthtogether.ui.register.registermodel.ConfirmOTPViewModel
import healthtogether.ui.register.registermodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: ConfirmOTPViewModel by viewModels()
        super.onCreate(savedInstanceState)
        setContent {

            SetStatusBarColor(AppColor.Primary)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = AuthScreen.Auth.route) {
                navigation(
                    startDestination = AuthScreen.Login.route,
                    route = AuthScreen.Auth.route
                ) {
                    composable(AuthScreen.Login.route) {
                        LoginView(LoginPageViewModel(), navController)
                    }

                    composable(AuthScreen.RegisterScreen.route) {
                        RegisterView(RegisterViewModel(), navController)
                    }

                    composable(AuthScreen.RegisterMemberScreen.route) {
                        RegisterMemberFormView(navController)
                    }

                    composable(AuthScreen.RegisterTrainerScreen.route) {
                        RegisterTrainerFormView(navController)
                    }

                    composable(AuthScreen.RegisterCompletedScreen.route) {
                        RegisterCompletedPage(navController)
                    }

                    composable(AuthScreen.RegisterConfirmOTPScreen.route) {
                        val dataJson = it.arguments?.getString("data")
                        RegisterConfirmOTPPage(viewModel, dataJson, navController)
                    }

                    composable(AuthScreen.ForgotPasswordScreen.route) {
                        ForgetPassword(ForgetPasswordViewModel(), navController)
                    }

                    composable(AuthScreen.SendPasswordScreen.route) {
                        SendPasswordPage(navController)
                    }

                    composable(AuthScreen.FaceScanScreen.route){
                        FaceScanView(navController)
                    }
                }

                navigation(
                    startDestination = "main_app",
                    route = "main"
                ) {

                    composable("main_app") {
//                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)
                        Button(onClick = {
                            navController.navigate("outdoor") {
                                popUpTo("main") {
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(text = "to outdoor")
                        }

                    }

                    composable("outdoor") {
//                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)

                    }

                    composable("indoor") {
//                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)

                    }
                }
            }
//            LoginNavigation()
        }
    }
}
