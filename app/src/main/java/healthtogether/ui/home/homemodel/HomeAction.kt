package healthtogether.ui.home.homemodel

import healthtogether.redux.Action

sealed class HomeAction : Action {
    data class ProvinceChanged(val newProvince: String) : HomeAction()
    data class DistrictChanged(val newDistrict: String) : HomeAction()
    data class SubDistrictChanged(val newSubDistrict: String) : HomeAction()
    data class PlaceChanged(val newPlace: String) : HomeAction()
    object OutdoorExerciseButtonClicked : HomeAction()
    object ExerciseAtHomeButtonClicked : HomeAction()
    object FriendIconCLicked : HomeAction()
    object ChatIconClicked : HomeAction()
    object HistoryIconClicked : HomeAction()
    object SettingIconClicked : HomeAction()
    object HomeIconClicked : HomeAction()
}