package com.mheredia.petproject.data.model

data class Contacts(
    val userId:String,
    var contactName:String="",
    var contactPhone:String="",
    var contactEmail:String="",
    var notes:String=""
)