package com.pjyotianwar.securechat.models

class Persons {
    var uid: String = ""
    var mail: String = ""
    var displayName: String? = ""
    var photoUrl: String? = ""


    constructor(){}

    constructor(u: String, m: String, d: String, p: String){
        this.uid = u
        this.mail = m
        this.displayName = d
        this.photoUrl = p
    }
}
//{
//    companion object Person{
//        fun newInstance(): Persons = Persons("", "", "", "")
//    }
//}