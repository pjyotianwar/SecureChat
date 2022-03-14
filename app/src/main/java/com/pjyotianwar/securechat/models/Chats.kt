package com.pjyotianwar.securechat.models

class Chats {
    var sId: String=""
    var rId: String=""
    var chatId: String=if(sId.compareTo(rId)>0) rId+sId else sId+rId
    var createdAt: Long = 0L
    var message: String = ""

    constructor(){}

    constructor(s: String, r: String, c: String, ca: Long=0L, m: String=""){
        this.sId = s
        this.rId = r
        this.chatId = c
        this.createdAt = ca
        this.message = m
    }
}