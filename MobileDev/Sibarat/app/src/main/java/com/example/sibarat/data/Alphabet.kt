package com.example.sibarat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Alphabet(
    val alphabet: String,
    val photo: Int
): Parcelable


