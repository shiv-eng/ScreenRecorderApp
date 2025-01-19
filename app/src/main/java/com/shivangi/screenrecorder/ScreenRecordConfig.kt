package com.shivangi.screenrecorder

import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenRecordConfig(
    val resultCode: Int,
    val data: Intent
) : Parcelable
