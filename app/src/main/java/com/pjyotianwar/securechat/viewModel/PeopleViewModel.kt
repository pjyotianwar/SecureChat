package com.pjyotianwar.securechat.viewModel

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pjyotianwar.securechat.encrypt
import com.pjyotianwar.securechat.models.Chats
import com.pjyotianwar.securechat.models.LastChat
import com.pjyotianwar.securechat.models.Persons

class PeopleViewModel : ViewModel() {

    private val pkey = "asdfgh"

    var msg =  mutableStateOf(TextFieldValue(""))

    fun changeMsg(m: TextFieldValue){
        msg.value = m
    }

    fun signOut(){
        from = -1
        senderIndex = -1
        receiverIndex = -1
    }

    val myref= Firebase.database.getReference("user")
    private val persons = mutableStateListOf<Persons>()

    var receiverIndex = -1
    var from = -1
    var senderIndex = -1

    init {
        loadUsers()
    }

    fun findSenderIndex(): Int{
        findSender()
        return senderIndex
    }

    fun onBack(){
        receiverIndex = -1
        from = -1
        msg.value = TextFieldValue("")
    }

    fun fetchSender(): Persons{
        loadUsers()
        var p = Persons()
        myref.child("${Firebase.auth.uid}").get().addOnSuccessListener {
            Log.v("People fetchSender", "${it}")
            for (i in it.children)
                p = it.getValue(Persons::class.java)!!
            Log.v("People fetchSender", "${p.displayName}")
        }
        return p
    }

    fun findSender(): Persons{
        loadUsers()
        var ind = 0
        for (p in persons){
            if (p.uid == Firebase.auth.uid){
                senderIndex = ind
                break
            }
            ind+=1
        }
        return persons[senderIndex]
    }

    fun getPersons(): List<Persons> {
        return persons
    }

    private fun loadUsers() {
        Log.d("loadUsers", "")
        myref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                persons.clear()
                Log.v("loadUsers", "$snapshot")
                for (user in snapshot.children) {
                    val u = user.getValue(Persons::class.java)
                    Log.v("loadUsers", "$u")
                    persons.add(u!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.v("User", "Error")
            }
        }
        )
    }

    fun putNewReceiver(p: Int){
        receiverIndex = p
        from = 0
    }

    fun fetchReceiver(): Persons{
        if (from==0){
            return persons[receiverIndex]
        }
        else if(from == 1){
            return lastChats[receiverIndex].peer
        }
        else{
            return Persons()
        }
    }

    //Last Chats

    private val lastChats= mutableStateListOf<LastChat>()

    val mylastchatref= Firebase.database.getReference("lastChat")

    fun getLastChat(): List<LastChat> {
        loadLastChats()
        return lastChats
    }

    fun loadLastChats() {
        mylastchatref.child("${Firebase.auth.uid}").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                lastChats.clear()
                for (user in snapshot.children) {
                    val u = user.getValue(LastChat::class.java)
                    lastChats.add(u!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.v("LastConversation", "Error")
            }
        }
        )
    }

    fun updateLastChat(msg: String){
        val snd = findSender()
        val rec = if (from == 1){
            lastChats[receiverIndex].peer
        }
        else{
            persons[receiverIndex]
        }
        val lastChatRec = LastChat( rec, msg, System.currentTimeMillis())

        mylastchatref.child("${snd.uid}").child("${rec.uid}").setValue(lastChatRec)
        mylastchatref.child("${rec.uid}").child("${snd.uid}").setValue(LastChat(snd, msg, System.currentTimeMillis()))
        Log.v("LastChatViewModel", "Sent")
    }

    fun putLastReceiver(p: Int){
        receiverIndex = p
        from = 1
    }

    private val Chat= mutableStateListOf<Chats>()

    val mychatref= Firebase.database.getReference("chat")

    fun getChat(): List<Chats> {
        loadChats()
        return Chat
    }


    private fun loadChats() {
        val sId = findSender().uid
        val rId = fetchReceiver().uid

        val cId = if(sId.compareTo(rId)>0) rId+sId else sId+rId

        mychatref.child("$cId").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                Chat.clear()
                for (cht in snapshot.children) {
                    val c = cht.getValue(Chats::class.java)
                    if (c?.chatId.equals(cId)) {
                        Chat.add(c!!)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
//                Log.v("ChatViewModel", "Error")
            }
        }
        )
    }

    fun onSendButtonClicked(){
        val sId = findSender().uid
        val rId = fetchReceiver().uid
        val cId = if(sId.compareTo(rId)>0) rId+sId else sId+rId
        val etxt = encrypt(msg.value.text, pkey)
        val cts = Chats(s = sId, r = rId, c = cId, ca = System.currentTimeMillis(), m = etxt.toString()) //msg.value.text
        mychatref.child(cts.chatId).push().setValue(cts)
        updateLastChat(etxt.toString())
        msg.value = TextFieldValue("")
    }
}