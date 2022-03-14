package com.pjyotianwar.securechat.models

class LastChat{
//    var uid: String? = "",
//    var displayName: String?="",
//    var photoUrl: String? = "",
    var peer: Persons = Persons()
    var lastMessage: String? = ""
    var time: Long? = 0L

    constructor(p: Persons, l: String, t: Long=0L){
        this.peer = p
        this.lastMessage = l
        this.time = t
    }

    constructor()  {}


//    @Exclude
//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "uid" to uid,
//            "photoUrl" to photoUrl,
//            "lastMessage" to lastMessage,
//            "time" to time,
//            "displayName" to displayName
//        )
//    }
}