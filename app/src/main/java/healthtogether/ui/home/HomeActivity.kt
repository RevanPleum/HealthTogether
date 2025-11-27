package healthtogether.ui.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import dagger.hilt.android.AndroidEntryPoint
import healthtogether.components.AppBackground
import healthtogether.components.ExerciseAtHomeScreen
import healthtogether.components.HomeScreen
import healthtogether.components.OutdoorExerciseScreen
import healthtogether.components.SettingScreen
import healthtogether.components.ShowLoading
import healthtogether.ui.chat.ChatFriendView
import healthtogether.ui.chat.ChatListView

import healthtogether.ui.friend.FriendListView
import healthtogether.ui.friend.OtherProfileView
import healthtogether.ui.history.HistoryView
import healthtogether.ui.home.homemodel.HomeViewModel
import healthtogether.ui.liveathome.CreateLiveRoomView
import healthtogether.ui.liveathome.LiveRoomView
import healthtogether.ui.liveathome.RatingLiveView
import healthtogether.ui.liveathome.SelectLiveRoomView
import healthtogether.ui.liveathome.livemodel.LiveViewModel
import healthtogether.ui.outdoorexercise.CreateGroupView
import healthtogether.ui.outdoorexercise.GoogleMapView
import healthtogether.ui.outdoorexercise.GroupRoomView
import healthtogether.ui.outdoorexercise.MemberListView
import healthtogether.ui.outdoorexercise.ReportGroupView
import healthtogether.ui.outdoorexercise.SelectGroupView
import healthtogether.ui.outdoorexercise.SelectPlaceView
import healthtogether.ui.outdoorexercise.outdoormodel.OutdoorRoomSearchData
import healthtogether.ui.outdoorexercise.outdoormodel.channels.ChannelListViewModel
import healthtogether.ui.register.UpgradeToTrainerView
import healthtogether.ui.register.registermodel.RegisterViewModel
import healthtogether.ui.setting.AboutAppView
import healthtogether.ui.setting.EditProfileView
import healthtogether.ui.setting.ProfileView
import healthtogether.ui.setting.SettingView
import io.getstream.chat.android.compose.viewmodel.messages.AttachmentsPickerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageComposerViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import live.videosdk.rtc.android.VideoSDK

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {


    @SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: HomeViewModel by viewModels()
        val channel: ChannelListViewModel by viewModels()
        val liveViewModel: LiveViewModel by viewModels()

        VideoSDK.initialize(applicationContext)
        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID)
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)


        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        val tokenAuthen = intent.getStringExtra("tokenAuth")
        val authenCheck = intent.getBooleanExtra("isAuthenticated", false)
        val userName = intent.getStringExtra("userName")
        val userID = intent.getStringExtra("userId")
        val userStatus = intent.getStringExtra("userStatus")
        val chatToken = intent.getStringExtra("chatId")


        Log.v(
            "TestGetExtra",
            "tokenAu : $tokenAuthen , authenCheck : $authenCheck , userName : $userName , userStatus : $userStatus , chatToken : $chatToken"
        )


        //viewModel.TestQuickLogin("test1", "test2")

        setContent {
            val navController = rememberNavController()


            AppBackground()

            NavHost(navController = navController, startDestination = HomeScreen.Home.route) {
                navigation(
                    startDestination = HomeScreen.MainHomeScreen.route,
                    route = HomeScreen.Home.route
                ) {

                    composable(HomeScreen.MainHomeScreen.route) {
                        var isLoading by remember {
                            mutableStateOf(false)
                        }

                        if(channel.getUser().getCurrentUser() == null)
                            LaunchedEffect(Unit) {
                                isLoading = true
                                viewModel.RealLogin(userID!!,chatToken!!)

                                if (channel.getUser().getCurrentUser()?.extraData?.get("age") == null)
                                    viewModel.setNewUserData(userID,"","","18","NONE","MEMBER","","")

                                viewModel.setProfileData(channel.getUser().getCurrentUser()!!)

                                isLoading = false
                            }


                        if(isLoading)
                            ShowLoading()
                        else
                            HomeView(navController = navController, viewModel)

                    }


                    composable(HomeScreen.FriendListScreen.route) {
                        Log.v("testPage", "FriendPage")
                        FriendListView(navController = navController, viewModel = viewModel)

                    }


                    composable(HomeScreen.ChatListScreen.route) {
                        Log.v("testPage", "ChatPage")
                        ChatListView(navController = navController, viewModel = viewModel)


                    }


                    composable(HomeScreen.HistoryScreen.route) {

                        if (viewModel.state.isLoading) {
                            ShowLoading()
                        } else {
                            Log.v("testPage", "HistoryPage")
                            HistoryView(navController = navController, viewModel, channel)
                        }
                    }

                    composable(HomeScreen.ProfileScreen.route) {

                        Log.v("testPage", "ProfilePage")
                        ProfileView(navController = navController, viewModel)
                    }

                    composable(HomeScreen.EditProfileScreen.route) {
                        Log.v("testPage", "EditProfilePage")
                        EditProfileView(navController = navController, viewModel)
                    }

                    composable(HomeScreen.ChatFriendScreen.route){
                        //Build the ViewModel factory
                        val factory =
                            MessagesViewModelFactory(
                                context = this@HomeActivity,
                                channelId = it.arguments?.getString("roomID").toString(),
                            )

                        //Build the required ViewModels, using the 'factory'
                        val listViewModel =
                            viewModel(MessageListViewModel::class.java, factory = factory)
                        val attachmentsPickerViewModel =
                            viewModel(AttachmentsPickerViewModel::class.java, factory = factory)
                        val composerViewModel =
                            viewModel(MessageComposerViewModel::class.java, factory = factory)

                        ChatFriendView(
                            navController,
                            viewModel,
                            listViewModel,
                            attachmentsPickerViewModel,
                            composerViewModel,
                            it.arguments?.getString("friendID").toString()
                        )
                    }

                    composable(HomeScreen.UpgradeToTrainerScreen.route){
                        UpgradeToTrainerView(RegisterViewModel(),navController)
                    }
                }

                navigation(
                    startDestination = OutdoorExerciseScreen.SelectPlace.route,
                    route = OutdoorExerciseScreen.OutdoorExercise.route
                ) {
                    composable(OutdoorExerciseScreen.SelectPlace.route) {
                        SelectPlaceView(navController = navController, viewModel, channel)
                    }

                    composable(OutdoorExerciseScreen.SelectGroup.route) {
                        var searchData by mutableStateOf(OutdoorRoomSearchData())
                        searchData = searchData.copy(
                            province = it.arguments?.getString("province").toString(),
                            district = it.arguments?.getString("district").toString(),
                            subDistrict = it.arguments?.getString("subDistrict").toString(),
                            placeValue = it.arguments?.getString("selectPlace").toString(),
                            placeName = it.arguments?.getString("placeName").toString(),
                            roomName = it.arguments?.getString("roomName").toString(),
                            exercise = it.arguments?.getString("exercise").toString(),
                            gender = it.arguments?.getString("gender").toString()
                        )
                        SelectGroupView(
                            navController = navController,
                            viewModel,
                            channel,
                            searchData
                        )
                    }

                    composable(OutdoorExerciseScreen.GroupRoom.route) {
                        //Build the ViewModel factory
                        val factory =
                            MessagesViewModelFactory(
                                context = this@HomeActivity,
                                channelId = it.arguments?.getString("groupRoomID").toString(),
                            )

                        //Build the required ViewModels, using the 'factory'
                        val listViewModel =
                            viewModel(MessageListViewModel::class.java, factory = factory)
                        val attachmentsPickerViewModel =
                            viewModel(AttachmentsPickerViewModel::class.java, factory = factory)
                        val composerViewModel =
                            viewModel(MessageComposerViewModel::class.java, factory = factory)

                        GroupRoomView(
                            navController,
                            viewModel,
                            channel,
                            listViewModel,
                            attachmentsPickerViewModel,
                            composerViewModel
                        )
                    }

                    composable(OutdoorExerciseScreen.EditMarkerLocation.route) {
                        GoogleMapView(
                            this@HomeActivity,
                            navController,
                            channel,
                            it.arguments?.getString("groupRoomID")
                        )
                    }

                    composable(OutdoorExerciseScreen.CreateGroup.route) {
                        val data = it.arguments?.getString("searchData")
                        val searchData: OutdoorRoomSearchData =
                            Json.decodeFromString<OutdoorRoomSearchData>(data.toString())

                        if (searchData != null) {
                            CreateGroupView(navController, viewModel, channel, searchData)
                        }
                    }

                    composable(OutdoorExerciseScreen.MemberList.route) {
                        //Build the ViewModel factory
                        val factory =
                            MessagesViewModelFactory(
                                context = this@HomeActivity,
                                channelId = it.arguments?.getString("groupRoomID").toString(),
                            )


                        //Build the required ViewModels, using the 'factory'
                        val listViewModel =
                            viewModel(MessageListViewModel::class.java, factory = factory)
                        MemberListView(
                            navController = navController,
                            viewModel = viewModel,
                            messageViewModel = listViewModel,

                        )


                    }

                    composable(OutdoorExerciseScreen.MemberProfile.route) {
                        OtherProfileView(
                            navController = navController,
                            viewModel = viewModel,
                            id = it.arguments?.getString("memberID").toString()
                        )
                    }

                    composable(OutdoorExerciseScreen.ReportGroup.route) {
                        //Build the ViewModel factory
                        val factory =
                            MessagesViewModelFactory(
                                context = this@HomeActivity,
                                channelId = it.arguments?.getString("groupRoomID").toString(),
                            )


                        //Build the required ViewModels, using the 'factory'
                        val listViewModel =
                            viewModel(MessageListViewModel::class.java, factory = factory)
                        MemberListView(
                            navController = navController,
                            viewModel = viewModel,
                            messageViewModel = listViewModel
                        )

                        ReportGroupView(
                            navController = navController,
                            viewModel = viewModel,
                            messageViewModel = listViewModel
                        )
                    }

                }

                navigation(
                    startDestination = ExerciseAtHomeScreen.LiveRoomList.route,
                    route = ExerciseAtHomeScreen.ExerciseAtHome.route
                ) {
                    composable(ExerciseAtHomeScreen.LiveRoomList.route) {

                        SelectLiveRoomView(navController,viewModel,channel)
                    }

                    composable(ExerciseAtHomeScreen.CreateLiveRoom.route){
                        CreateLiveRoomView(navController,viewModel,liveViewModel,channel)
                    }

                    composable(ExerciseAtHomeScreen.LiveRoom.route) {
                        //Build the ViewModel factory
                        val factory =
                            MessagesViewModelFactory(
                                context = this@HomeActivity,
                                channelId = it.arguments?.getString("groupRoomID").toString(),
                            )

                        //Build the required ViewModels, using the 'factory'
                        val listViewModel =
                            viewModel(MessageListViewModel::class.java, factory = factory)
                        val attachmentsPickerViewModel =
                            viewModel(AttachmentsPickerViewModel::class.java, factory = factory)
                        val composerViewModel =
                            viewModel(MessageComposerViewModel::class.java, factory = factory)

                        LiveRoomView(navController = navController, viewModel = viewModel, channel = channel, message = listViewModel, attachment =  attachmentsPickerViewModel, composer = composerViewModel, liveViewModel = liveViewModel, context = this@HomeActivity)
                    }

                    composable(ExerciseAtHomeScreen.RatingTrainer.route) {
                        val factory =
                            MessagesViewModelFactory(
                                context = this@HomeActivity,
                                channelId = it.arguments?.getString("groupRoomID").toString(),
                            )

                        val listViewModel =
                            viewModel(MessageListViewModel::class.java, factory = factory)

                        RatingLiveView(navController = navController, viewModel = viewModel, messageViewModel = listViewModel)

                    }

                    composable("showMember") {

                    }

                    composable("reportScreen") {

                    }

                }

                navigation(
                    startDestination = SettingScreen.MainSettingScreen.route,
                    route = SettingScreen.Setiing.route
                ) {
                    composable(SettingScreen.MainSettingScreen.route) {
                        //viewModel.TestUpdateFriend()

                        SettingView(navController = navController, state = viewModel.state, viewModel)
                    }
                    composable(SettingScreen.ChangePasswordScreen.route) {

                    }
                    composable(SettingScreen.BlockListScreen.route) {

                    }
                    composable(SettingScreen.AboutAppScreen.route) {
                        AboutAppView(navController = navController, state = viewModel.state)
                    }



                }

            }

        }
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                REQUESTED_PERMISSIONS, requestCode)
            return false
        }
        return true
    }

    companion object {
        private const val PERMISSION_REQ_ID = 22
        private val REQUESTED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )
    }

    @Composable
    inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
        val navGraphRoute = destination.parent?.route ?: return viewModel()
        val parentEntry = remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
        return viewModel(parentEntry)
    }
}