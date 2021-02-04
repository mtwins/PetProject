package com.mheredia.petproject.ui.petInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PetInfoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is pet info Fragment"
    }
    val text: LiveData<String> = _text
}