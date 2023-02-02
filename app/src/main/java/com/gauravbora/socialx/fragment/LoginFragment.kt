package com.gauravbora.socialx.fragment

import android.content.Intent
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
import com.gauravbora.socialx.HomeActivity
import com.gauravbora.socialx.R
import com.gauravbora.socialx.databinding.FragmentLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var email : String
    private lateinit var pass : String
    private lateinit var editText: EditText
    private lateinit var password : EditText

    companion object {
        fun newInstance(email: String, pass: String): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            args.putString("email", email)
            args.putString("pass",pass)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        editText = binding.emailEt
        password = binding.passwordEt

        email = arguments?.getString("email").toString()
        pass = arguments?.getString("pass").toString()

        facebookLogin()
        googleLogin()
        registerNow()
        loginBtn()

        mAuth = FirebaseAuth.getInstance()

        return binding.root
    }
    private fun loginBtn(){
        binding.loginBtn.setOnClickListener {
            val emailNew = editText.text.toString()
            val passNew = password.text.toString()
            if (emailNew == email && passNew == pass){
                Toast.makeText(context, " SUCCESS", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
            }
            else if(emailNew != email){
                Toast.makeText(context, "Check Email", Toast.LENGTH_SHORT).show()
            }
            else if(passNew != pass){
                Toast.makeText(context, "Check Password", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerNow(){
        binding.registerTv.setOnClickListener {
            val signUpFragment = SignUpFragment()
            val fragmentManager : FragmentManager? = fragmentManager
            val fragmentTransition : FragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransition.add(R.id.frame_layout,signUpFragment)
            fragmentTransition.commit()
        }
    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,120)
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
//                    success
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                }else{
//                   failed
                }
            }
    }

    private fun facebookLogin(){
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(error: FacebookException) {
                    // App code
                }
            })
        binding.facebookIv.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@LoginFragment, listOf("public_profile"))
        }
    }

    private fun googleLogin(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = context?.let { GoogleSignIn.getClient(it,gso) }!!

        binding.googleIv.setOnClickListener {
            signIn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // callback for facebook response to manager
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 120){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken)
                }catch (e : ApiException){
//                    fail
                }
            }else{

            }
        }
    }
}