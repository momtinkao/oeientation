package com.example.oeientation

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        val bt_login = binding.login
        val bt_password = binding.passwordview
        val accountinput = binding.accountinput
        val passwordinput = binding.passwordinput
        val account = binding.account
        val password = binding.password
        val bt_r = binding.button
        val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[^ ]{8,16}\$")
        var ac_flag = false
        var ps_flag = false

        bt_login.setOnClickListener{
            startActivity(Intent(this,Login::class.java))
        }

        bt_password.setOnCheckedChangeListener{_,isChecked->
            if(isChecked){
                passwordinput.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }
            else{
                //否則隱藏密碼
                passwordinput.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }




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
                        Toast.makeText(applicationContext,"帳號創立成功",Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }


        }

    }
}