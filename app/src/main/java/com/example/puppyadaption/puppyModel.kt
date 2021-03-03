package com.example.puppyadaption

import com.google.firebase.Timestamp
import java.util.*

data class puppyModel(
    var id: String,
    val puppy_breed: String,
    val owner_name: String,
    val age: String,
    val gender: String,
    val other_description: String,
    val location: String,
    val image_url: String
) {
    constructor() : this("", "", "", "",
        "", "", "", "")
}
