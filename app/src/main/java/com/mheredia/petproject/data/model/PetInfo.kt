package com.mheredia.petproject.data.model

data class PetInfo(
    val userId:String,
    var petName:String="",
    var petType:String="",
    var petBreed:String="",
    var petProfilePictureId:String=""
)