package healthtogether.components

import android.util.Log


sealed class AuthScreen(val route: String) {
    object Auth : AuthScreen("auth")
    object Login : AuthScreen("login")
    object RegisterScreen : AuthScreen("register")
    object RegisterMemberScreen : AuthScreen("register_member")
    object RegisterTrainerScreen : AuthScreen("register_trainer")
    object RegisterConfirmOTPScreen : AuthScreen("register_confirm_otp?token={data}")
    object RegisterCompletedScreen : AuthScreen("register_completed")
    object ForgotPasswordScreen : AuthScreen("forgot_password")
    object SendPasswordScreen : AuthScreen("send_password")
    object FaceScanScreen : AuthScreen("face_scan_screen")

}

sealed class HomeScreen(val route: String) {
    object Home : HomeScreen("home")
    object MainHomeScreen : HomeScreen("main_home_screen")
    object ChatListScreen : HomeScreen("chat_list_screen")
    object FriendListScreen : HomeScreen("friend_list_screen")
    object HistoryScreen : HomeScreen("history_screen")
    object SettingScreen : HomeScreen("setting_screen")
    object ProfileScreen : HomeScreen("Profile_Screen")
    object EditProfileScreen : HomeScreen("edit_profile_screen")
    object ChatFriendScreen : HomeScreen("chat_friend_screen/{roomID}/{friendID}") {
        fun routeWithID(
            roomID: String,
            friendID: String
        ): String{
            return "chat_friend_screen/$roomID/$friendID"
        }
    }
    object UpgradeToTrainerScreen : HomeScreen("upgrade_to_trainer_screen")
}

sealed class SettingScreen(val route: String) {
    object Setiing : SettingScreen("setting")
    object MainSettingScreen : SettingScreen("main_setting_screen")
    object ChangePasswordScreen : SettingScreen("change_password_screen")
    object BlockListScreen : SettingScreen("block_list_screen")
    object AboutAppScreen : SettingScreen("about_app_screen")

}

sealed class OutdoorExerciseScreen(val route: String) {
    object OutdoorExercise : OutdoorExerciseScreen("outdoor_exercise")
    object SelectPlace : OutdoorExerciseScreen("select_place_screen")
    object SelectGroup :
        OutdoorExerciseScreen("select_group_screen/{province}/{district}/{subDistrict}/{selectPlace}/{placeName}/{roomName}/{exercise}/{gender}") {
        fun routeWithSelectPlace(
            province: String,
            district: String,
            subDistrict: String,
            selectPlace: String,
            placeName: String,
            roomName: String,
            exercise: String,
            gender: String
        ): String {
            return "select_group_screen/$province/$district/$subDistrict/$selectPlace/$placeName/$roomName/$exercise/$gender"
        }
    }

    object EditMarkerLocation : OutdoorExerciseScreen("edit_marker_location_screen/{groupRoomID}") {
        fun routeWithGroupRoomId(groupRoomID: String): String {
            return "edit_marker_location_screen/$groupRoomID"
        }
    }

    object ReportGroup : OutdoorExerciseScreen("report_group_screen/{groupRoomID}") {
        fun routeWithGroupRoomId(groupRoomID: String): String {
            Log.v("TestGroup", "RouteWithID : ${groupRoomID}")
            return "report_group_screen/$groupRoomID"
        }
    }

    object CreateGroup : OutdoorExerciseScreen("create_group_screen/{searchData}") {

        fun routeWithSearchData(searchData: String): String {
            return "create_group_screen/$searchData"
        }
    }

    object GroupRoom : OutdoorExerciseScreen("group_room_screen/{groupRoomID}") {
        fun routeWithGroupRoomId(groupRoomID: String): String {
            Log.v("TestGroup", "RouteWithID : ${groupRoomID}")
            return "group_room_screen/$groupRoomID"
        }
    }

    object MemberList : OutdoorExerciseScreen("member_list_screen/{groupRoomID}") {
        fun routeWithGroupRoomId(groupRoomID: String): String {
            return "member_list_screen/$groupRoomID"
        }
    }

    object MemberProfile : OutdoorExerciseScreen("member_profile_screen/{memberID}") {
        fun routeWithMemberId(memberID: String): String {
            return "member_profile_screen/$memberID"
        }
    }
}

sealed class ExerciseAtHomeScreen(val route: String){
    object ExerciseAtHome : ExerciseAtHomeScreen("exercise_athome")

    object LiveRoomList : ExerciseAtHomeScreen("live_room_list_screen")

    object CreateLiveRoom : ExerciseAtHomeScreen("create_live_room_screen")

    object  LiveRoom : ExerciseAtHomeScreen("live_room_screen/{groupRoomID}"){
        fun routeWithGroupRoomId(groupRoomID: String): String {
            return "live_room_screen/$groupRoomID"
        }
    }

    object RatingTrainer : ExerciseAtHomeScreen("rating_screen/{groupRoomID}"){
        fun routeWithGroupRoomId(groupRoomID: String): String{
            return "rating_screen/$groupRoomID"
        }
    }


}

