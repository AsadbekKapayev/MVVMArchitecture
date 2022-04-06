package com.example.mvvmarchitectureroman

import android.app.Application
import com.example.mvvmarchitectureroman.model.UsersServices

class App : Application() {

    val usersServices = UsersServices()
}