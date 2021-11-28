package com.example.oeientation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.example.oeientation.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        val accountinput = binding.accountinput
        val passwordinput = binding.passwordinput
        val bt_l = binding.button
        val bt_password = binding.passwordview

        bt_password.setOnCheckedChangeListener{_,isChecked->
            if(isChecked){
                passwordinput.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }
            else{
                //否則隱藏密碼
                passwordinput.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }

        bt_l.setOnClickListener{
            auth.signInWithEmailAndPassword(accountinput.text.toString(),passwordinput.text.toString()).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"登入成功",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }

}