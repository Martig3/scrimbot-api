package model

data class SteamUser(
    val avatar: String,
    val avatarfull: String,
    val avatarhash: String,
    val avatarmedium: String,
    val communityvisibilitystate: Int,
    val lastlogoff: Int,
    val loccityid: Int,
    val loccountrycode: String,
    val locstatecode: String,
    val personaname: String,
    val personastate: Int,
    val personastateflags: Int,
    val primaryclanid: String,
    val profilestate: Int,
    val profileurl: String,
    val realname: String,
    val steamid: String,
    val timecreated: Int
)
