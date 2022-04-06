package com.example.mvvmarchitectureroman.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchitectureroman.R
import com.example.mvvmarchitectureroman.UserNotFoundException
import com.example.mvvmarchitectureroman.model.UserDetails
import com.example.mvvmarchitectureroman.model.UsersServices
import com.example.mvvmarchitectureroman.tasks.EmptyResult
import com.example.mvvmarchitectureroman.tasks.Result
import com.example.mvvmarchitectureroman.tasks.PendingResult
import com.example.mvvmarchitectureroman.tasks.SuccessResult

class UserDetailsViewModel(
    private val usersService: UsersServices,
    private val userId: Long
) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

    private val _actionGoBack = MutableLiveData<Event<Unit>>()
    val actionGoBack: LiveData<Event<Unit>> = _actionGoBack

    private val currentState: State get() = state.value!!

    init {
        _state.value = State(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
        loadUser()
    }

    fun deleteUser() {
        val userDetailsResult = currentState.userDetailsResult
        if (userDetailsResult !is SuccessResult) return
        _state.value = currentState.copy(deletingInProgress = true)
        usersService.deleteUser(userDetailsResult.data.user)
            .onSuccess {
                _actionShowToast.value = Event(R.string.user_deleted)
                _actionGoBack.value = Event(Unit)
            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    private fun loadUser() {
        // Do not start 2nd load user details request if we rotate
        // screen during loading details
        if (currentState.userDetailsResult !is EmptyResult) return

        _state.value = currentState.copy(userDetailsResult = PendingResult())

        usersService.getById(userId)
                .onSuccess {
                    _state.value = currentState.copy(userDetailsResult = SuccessResult(it))
                }
                .onError {
                    _actionShowToast.value = Event(R.string.cant_load_user_details)
                    _actionGoBack.value = Event(Unit)
                }
                .autoCancel()
    }

    data class State(
        val userDetailsResult: Result<UserDetails>,
        private val deletingInProgress: Boolean
    ) {

        val showContent: Boolean get() = userDetailsResult is SuccessResult
        val showProgress: Boolean get() = userDetailsResult is PendingResult || deletingInProgress
        val enableDeleteButton: Boolean get() = !deletingInProgress

    }
}