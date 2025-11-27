package healthtogether.ui.home


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.heathtogethermobile.R
import healthtogether.components.AuthScreen
import healthtogether.components.BottomMenu
import healthtogether.components.CategoryNavigateButton
import healthtogether.components.ExerciseAtHomeScreen
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.TextHeaderBoxScope
import healthtogether.components.TopAppNameAndProfile
import healthtogether.ui.AppTheme
import healthtogether.ui.home.homemodel.HomeViewModel

@Composable
fun HomeView(navController: NavController, viewModel: HomeViewModel) = AppTheme {

    Surface {
        Modifier.fillMaxSize()
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            TopAppNameAndProfile(navController, viewModel.state.imageProfile.toString())
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {

            Card(
                modifier = Modifier
                    .height(650.dp)
                    .padding(top = 30.dp),
                elevation = 20.dp,
                border = BorderStroke(0.dp, Color.White),
                shape = RoundedCornerShape(30.dp),

                ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),

                    ) {

                    Spacer(modifier = Modifier.size(20.dp))

                    TextHeaderBoxScope(
                        value = "Home", modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    CategoryNavigateButton(
                        buttonName = "Outdoor Exercise",
                        imageId = R.drawable.outdoorexercise,
                        screenRoute = OutdoorExerciseScreen.SelectPlace.route,
                        navController = navController
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    CategoryNavigateButton(
                        buttonName = "Exercise at Home",
                        imageId = R.drawable.athome,
                        screenRoute = ExerciseAtHomeScreen.LiveRoomList.route,//LiveRoom.routeWithGroupRoomId("messaging:TestCreateLiveOnApp1"),
                        navController = navController
                    )


                }
            }

        }


        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom

        ) {
            BottomMenu(viewModel.state, navController)
        }
    }
}


