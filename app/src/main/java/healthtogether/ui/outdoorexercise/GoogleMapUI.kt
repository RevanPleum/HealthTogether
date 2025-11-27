package healthtogether.ui.outdoorexercise

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import io.getstream.chat.android.client.api.models.QueryChannelRequest
import io.getstream.chat.android.client.events.MemberRemovedEvent
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.subscribeFor
import io.getstream.chat.android.client.utils.observable.Disposable
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel


@Composable
fun GoogleMapView(
    context: Context,
    navController: NavController,
    channel: ChannelListViewModel,
    roomId: String?
) {

    var exitRoom by remember { mutableStateOf(false) }

    var disposable: Disposable = channel.getUser().subscribeFor<MemberRemovedEvent>{
        if(it.user.id == channel.getUser().getCurrentUser()!!.id) {
            exitRoom = true
        }

    }

    LaunchedEffect(exitRoom) {
        if(exitRoom)
        {
            disposable.dispose()
            navController.popBackStack(OutdoorExerciseScreen.GroupRoom.route,true)
        }

    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        var hasLocationPermission by remember {
            mutableStateOf(checkForPermission(context))
        }
        var roomData by remember {
            mutableStateOf(Channel())
        }
        var finish by remember {
            mutableStateOf(false)
        }


        if (roomId != "None") {
            channel.getUser().queryChannel(
                "messaging", roomId.toString(),
                QueryChannelRequest().withMessages(10)
            ).enqueue { result ->
                if (result.isSuccess) {
                    finish = true
                    roomData = result.data()
                    Log.v("TestQuery", "Query: Success")
                } else {
                    Log.v("TestQuery", "Query: Fail")
                }

            }
        } else
            Log.v("TestQuery", "RoomID: None")



        if (hasLocationPermission) {

            if (roomId == "None" || finish) {
                Log.v("TestGoogleMap", "roomId = $roomId")
                MapScreen(context, navController, channel, if (roomId != "None") roomData else null)

            } else
                Log.v("TestGoogleMap", "roomData = null")
        } else {
            LocationPermissionScreen {
                hasLocationPermission = true
            }
        }
    }
}


@Composable
fun MapScreen(
    context: Context,
    navController: NavController,
    channel: ChannelListViewModel,
    roomData: Channel?
) {
    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var mapProperties by remember { mutableStateOf(MapProperties()) }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        if (roomData == null) {
            MyMapForCreateRoom(
                context = context,
                latLng = LatLng(location.latitude, location.longitude),
                navController = navController
            )

        } else {

            var roomLocation by remember {
                mutableStateOf(roomData.extraData.get("location") as? Map<String, Any>)
            }

            MyMap(
                context = context,
                latLng = if (roomLocation?.get("lat") == "None"
                    || roomLocation?.get("lng") == "None"
                )
                    LatLng(
                        location.latitude,
                        location.longitude
                    )
                else
                    LatLng(
                        roomLocation?.get("lat").toString().toDouble(),
                        roomLocation?.get("lng").toString().toDouble()
                    ),
                mapProperties = mapProperties,
                navController = navController,
                channel = channel,
                currentRoomData = roomData
            )
        }

    } else {
        Text(text = "Loading Map...")
    }
}


@Composable
fun MyMap(
    context: Context,
    latLng: LatLng,
    mapProperties: MapProperties = MapProperties(),
    navController: NavController,
    channel: ChannelListViewModel,
    currentRoomData: Channel
) {
    val latlang = remember {
        mutableStateOf(latLng)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapClick = {
                if (channel.getUser().getCurrentUser()?.id == currentRoomData.createdBy.id)
                    latlang.value = it
            }
        ) {
            Marker(
                state = MarkerState(position = latlang.value),
                title = "Location",
                snippet = "Marker in current location",
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            if (channel.getUser().getCurrentUser()?.id == currentRoomData.createdBy.id) {
                Button(onClick = {
                    channel.setLocation(
                        currentRoomData.id,
                        latlang.value.latitude,
                        latlang.value.longitude
                    )
                    navController.popBackStack()
                }) {
                    Text(text = "Confirm Marker")
                }
                Spacer(modifier = Modifier.width(4.dp))

            }

            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Back")
            }

            Spacer(modifier = Modifier.width(4.dp))

        }
    }
}


@Composable
fun MyMapForCreateRoom(
    context: Context,
    latLng: LatLng,
    mapProperties: MapProperties = MapProperties(),
    navController: NavController,
) {
    val latlang = remember {
        mutableStateOf(latLng)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapClick = {
                latlang.value = it
            }
        ) {
            Marker(
                state = MarkerState(position = latlang.value),
                title = "Location",
                snippet = "Marker in current location",
            )

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {

            Button(onClick = {
                navController.popBackStack()

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("lat", latlang.value.latitude)

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("lng", latlang.value.longitude)

            }) {
                Text(text = "Confirm Marker")
            }
            Spacer(modifier = Modifier.width(4.dp))



            Button(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Back")
            }

            Spacer(modifier = Modifier.width(4.dp))

        }
    }
}


@Composable
fun LocationPermissionScreen(
    onPermissionGranted: () -> Unit
) {
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var isGranted = true
        permissions.entries.forEach {
            if (!it.value) {
                isGranted = false
                return@forEach
            }
        }

        if (isGranted) {
            onPermissionGranted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Location Permission Required",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    )
                )
            }
        ) {
            Text(text = "Grant Location Permission")
        }
    }
}