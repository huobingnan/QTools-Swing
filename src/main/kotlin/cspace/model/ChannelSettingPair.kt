package cspace.model

data class ChannelSettingPair(
    var settingName: String,
    var settingValue: String
) {

    constructor(): this("", "")
}