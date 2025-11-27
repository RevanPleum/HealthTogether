package healthtogether.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation


class NavigationExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "auth") {
                navigation(
                    startDestination = "login",
                    route = "auth"
                ) {
                    composable("login") {
                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)

                        Button(onClick = {
                            navController.navigate("main") {
                                popUpTo("auth") {
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(text = "to main app")
                        }

                        Button(
                            onClick = {
                                navController.navigate("register") {

                                }

                            }, modifier = Modifier.padding(top = 50.dp)
                        ) {
                            Text(text = "to register")
                        }

                        Button(
                            onClick = {
                                navController.navigate("forget_password") {

                                }
                            }, modifier = Modifier.padding(top = 100.dp)
                        ) {
                            Text(text = "to forget")
                        }
                    }

                    composable("register") {
                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)
                        Button(onClick = {
                            navController.navigate("main") {
                                popUpTo("auth") {
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(text = "to main app")
                        }

                        Button(
                            onClick = {
                                navController.navigate("register") {

                                }

                            }, modifier = Modifier.padding(top = 50.dp)
                        ) {
                            Text(text = "(here)to register")
                        }

                        Button(
                            onClick = {
                                navController.navigate("forget_password") {

                                }
                            }, modifier = Modifier.padding(top = 100.dp)
                        ) {
                            Text(text = "to forget")
                        }
                    }

                    composable("forget_password") {
                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)
                        Button(onClick = {
                            navController.navigate("main") {
                                popUpTo("auth") {
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(text = "to main app")
                        }

                        Button(
                            onClick = {
                                navController.navigate("register") {

                                }

                            }, modifier = Modifier.padding(top = 50.dp)
                        ) {
                            Text(text = "to register")
                        }

                        Button(
                            onClick = {
                                navController.navigate("forget_password") {

                                }
                            }, modifier = Modifier.padding(top = 100.dp)
                        ) {
                            Text(text = "(here)to forget")
                        }
                    }
                }

                navigation(
                    startDestination = "main_app",
                    route = "main"
                ) {

                    composable("main_app") {
                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)
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
                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)

                    }

                    composable("indoor") {
                        val viewModel = it.sharedViewModel<NavigationViewModel>(navController)

                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}