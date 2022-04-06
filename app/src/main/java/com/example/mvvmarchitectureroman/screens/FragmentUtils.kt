package com.example.mvvmarchitectureroman.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmarchitectureroman.App
import com.example.mvvmarchitectureroman.Navigator
import java.io.PipedReader
import java.lang.IllegalStateException

    typealias ViewModelCreator = (App) -> ViewModel?

class ViewModelFactory(
    private val app: App,
    private val viewModelCreator: ViewModelCreator = { null }
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            UsersListViewModel::class.java -> {
                UsersListViewModel(app.usersServices)
            }
            else -> {
                viewModelCreator?: throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.creator(creator: ViewModelCreator) = ViewModelFactory(requireContext().applicationContext as App, creator)

fun Fragment.navigator() = requireActivity() as Navigator