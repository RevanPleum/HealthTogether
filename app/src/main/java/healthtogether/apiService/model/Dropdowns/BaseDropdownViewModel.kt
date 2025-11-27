package healthtogether.apiService.model.Dropdowns

data class BaseDropdownViewModel(
    val value: String = "",
    val text: String = "",
)

data class DropdownBodyViewModel(
    val provinceId: String,
    val districtId: String,
    val subDistrictId: String
)