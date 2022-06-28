package com.mohamed.medhat.photoweather.model

import com.mohamed.medhat.photoweather.utils.State

data class StateHolder(@State val state: Int, val error: String = "")