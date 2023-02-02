package com.gauravbora.socialx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.gauravbora.socialx.databinding.ActivityMainBinding
import com.gauravbora.socialx.fragment.LoginFragment
import com.gauravbora.socialx.fragment.SignUpFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginFragment = LoginFragment()
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransition : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransition.add(R.id.frame_layout,loginFragment)
        fragmentTransition.commit()

        binding.loginToggleBtn.setOnClickListener {
            changeFragments(LoginFragment())
        }

        binding.signupToggleBtn.setOnClickListener {
            changeFragments(SignUpFragment())
        }
    }
    private fun changeFragments(fragment: Fragment) {
        val fragmentManager : FragmentManager = supportFragmentManager
        val fragmentTransition : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout,fragment)
        fragmentTransition.commit()
    }
}