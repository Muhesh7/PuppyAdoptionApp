package com.example.puppyadaption

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class puppyViewModel : ViewModel() {

    val puppyList: MutableState<ArrayList<puppyModel>> = mutableStateOf(ArrayList())
    val puppyDetail: MutableState<puppyModel> = mutableStateOf(puppyModel())
    var imageUri = mutableStateOf<Uri?>(null)
    val db = Firebase.firestore
    val TAG = "RAJATH"
    val COLLECTION = "puppies"

    fun getKey(): String {
        return db.collection(COLLECTION).document().id
    }

    fun postPuppy(puppyMap: HashMap<String, String>, id: String) =
        db.collection(COLLECTION).document(id)
            .set(puppyMap)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    fun getPuppyList() =
        db.collection(COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val list: ArrayList<puppyModel> = arrayListOf()
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var pups: puppyModel = document.toObject(puppyModel::class.java)
                    pups.id = document.id
                    list.add(pups)
                }
                puppyList.value = list
            }

    fun getPuppyDetails(id: String) =
        db.collection(COLLECTION)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "${document.id} => ${document.data}")
                if (document.toObject(puppyModel::class.java) != null)
                    puppyDetail.value = document.toObject(puppyModel::class.java)!!
                else
                    Log.d(TAG, "document is null")
            }

    fun listenPuppies() =
        db.collection(COLLECTION)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }
                val list: ArrayList<puppyModel> = arrayListOf()
                for (document in snapshots!!.documentChanges) {
                    if (document.type == DocumentChange.Type.ADDED || document.type == DocumentChange.Type.MODIFIED) {
                        Log.d(TAG, "New city: ${document.document.data}")
                        list.add(document.document.toObject(puppyModel::class.java))
                    }
                }
                puppyList.value = list
            }


}