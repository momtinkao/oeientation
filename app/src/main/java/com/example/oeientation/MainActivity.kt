package com.example.oeientation

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.oeientation.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        val accountinput = binding.accountinput
        val passwordinput = binding.passwordinput
        val account = binding.account
        val password = binding.password
        val bt_r = binding.button
        val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[^ ]{8,16}\$")
        var ac_flag = false
        var ps_flag = false

        bt_r.setOnClickListener{
            if(accountinput.text.toString() == ""){
                account.helperText = "請輸入帳號"
            }
            else
            {
                account.helperText =""
                ac_flag = true
            }
            if(!pattern.matches(passwordinput.text.toString())){
                password.helperText = "密碼為8-16位數且至少包含一個大寫英文小寫英文及數字"
            }
            else
            {
                ps_flag = true
                password.helperText =""
            }
            if(ac_flag == true && ps_flag == true)
            {
                auth.createUserWithEmailAndPassword(accountinput.text.toString(),passwordinput.text.toString()).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val intent= Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }


        }

    }
}