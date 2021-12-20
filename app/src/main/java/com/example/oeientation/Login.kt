package com.example.oeientation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.example.oeientation.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var  auth: FirebaseAuth
    private lateinit var database: DatabaseReference


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
                    val user = Firebase.auth.currentUser
                    var uid = ""
                    user?.let {
                        uid = user.uid
                    }
                    database = Firebase.database.reference
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(!dataSnapshot.exists()){
                                database.child("users").setValue(uid)
                            }
                            else{
                                database.child("name").setValue("cisco")
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                        }

                    })
                    startActivity(Intent(this,teacher::class.java))
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }

}