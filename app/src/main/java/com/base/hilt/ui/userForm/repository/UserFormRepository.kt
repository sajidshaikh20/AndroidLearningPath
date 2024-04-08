package com.base.hilt.ui.userForm.repository

import android.util.Log
import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ApiInterface
import com.base.hilt.ui.userForm.model.UserData
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.base.hilt.utils.PrefKey.EMPTY
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

class UserFormRepository @Inject constructor(
    var pref: MyPreference
) : BaseRepository() {

    private val gson = Gson()
    fun submitUserData(userData: UserData) {
        // Implement submitting user data to API or database
        val json = gson.toJson(userData)
        pref.setValueString(PrefKey.USERMODEL, json)

    }

    fun getUserData(): Flow<UserData> {
        val  json  = pref.getValueString(PrefKey.USERMODEL,"")
        val flow = flow<UserData> {
            val data = gson.fromJson(json, UserData::class.java)
            if (data!=null){
                emit(data)
            }
        }
        return flow
    }
}