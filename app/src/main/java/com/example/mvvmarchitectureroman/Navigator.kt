package com.example.mvvmarchitectureroman

import com.example.mvvmarchitectureroman.model.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun toast(messageRes: Int)

}