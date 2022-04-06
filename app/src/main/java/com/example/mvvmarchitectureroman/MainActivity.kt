package com.example.mvvmarchitectureroman

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarchitectureroman.databinding.ActivityMainBinding
import com.example.mvvmarchitectureroman.model.User
import com.example.mvvmarchitectureroman.model.UsersListener
import com.example.mvvmarchitectureroman.model.UsersServices
import com.example.mvvmarchitectureroman.screens.UserDetailsFragment
import com.example.mvvmarchitectureroman.screens.UsersListFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, UsersListFragment())
                    .commit()
        }
    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(user.id))
                .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this, messageRes,Toast.LENGTH_SHORT).show()
    }
}