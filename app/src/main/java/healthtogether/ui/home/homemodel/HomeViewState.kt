package healthtogether.ui.home.homemodel

import healthtogether.redux.State

data class HomeViewState(
    //val homeInfo: HomeInfo? = null,
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val userID: String = "D3E071F4-E2A4-4C89-9468-11832285633C",
    val userName: String = "Test Name",
    val userGender: String = "Hidden",
    val userAge: Int = 18,
    val userExerciseFav: List<String> = emptyList(),
    val userPlaceFav: List<String> = emptyList(),
    val role: String = "00",
    val imageProfile: String = ""
) : State