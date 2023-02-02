package com.gauravbora.socialx.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.gauravbora.socialx.R
import com.gauravbora.socialx.databinding.FragmentSignUpBinding

@Suppress("DEPRECATION")
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var editText:EditText
    private lateinit var password:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater,container,false)

        editText = binding.emailSignupEt
        password = binding.passSignupEt

        binding.signInBtn.setOnClickListener {
            if (binding.checkBox.isChecked){
                val fragmentManager : FragmentManager? = fragmentManager
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_layout, LoginFragment())?.commit()

                sendData()

            }else{
                Toast.makeText(context, "Please check the checkbox first", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signTv.setOnClickListener {
            val loginFragment = LoginFragment()
            val fragmentManager : FragmentManager? = fragmentManager
            val fragmentTransition : FragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransition.add(R.id.frame_layout,loginFragment)
            fragmentTransition.commit()
        }

        binding.checkBox.isChecked = false
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "Checkbox is checked", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Checkbox is unchecked", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun sendData() {
        val email = editText.text.toString()
        Log.d("email-2", "email signIn: $email")
        val pass = password.text.toString()
        Log.d("pass-2", "pass signIn: $pass")

        val fragment = LoginFragment.newInstance(email,pass)
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}