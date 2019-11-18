package com.thanhhuy.mapboxdemoapp.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.thanhhuy.mapboxdemoapp.Class.User
import com.thanhhuy.mapboxdemoapp.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        navigate_main_activity_btn.setOnClickListener {
            val intent = Intent(this, BackgroundActivity::class.java)
            startActivity(intent)
        }


        register_button.setOnClickListener {
            val name = register_username_text_view.text.toString()
            val email = register_email_text_view.text.toString()
            val password = register_password_text_view.text.toString()
            val devices_code = devices_code.text.toString()
//            val phone_2 = register_phone_text_view_2.text.toString()
//            val phone_3 = register_phone_text_view_3.text.toString()
            if(name.isEmpty() || email.isEmpty() || password.isEmpty() || devices_code.isEmpty())
            {
                Toast.makeText(this,"Xin vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("RegisterActivity","name:"+ name )
            Log.d("RegisterActivity","email:"+ email)
            Log.d("RegisterActivity","password:"+ password)
            Log.d("RegisterActivity","phone_1:"+ devices_code)
//            Log.d("RegisterActivity","phone_2:"+ phone_2)
//            Log.d("RegisterActivity","phone_3:"+ phone_3)


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("RegisterActivity", "signInWithEmail:success : ${it.result.user.uid}")

                            saveUserToFirebaseDatabase()

                        }else {
                            // If sign in fails, display a message to the user.
                            Log.w("RegisterActivity", "signInWithEmail:failure", it.exception)
                            Toast.makeText(this,"Please enter your email,username or password: ${it.exception}", Toast.LENGTH_SHORT).show()
                        }
                        // ...
                    }
        }
    }
    private fun saveUserToFirebaseDatabase() {

        val uid = FirebaseAuth.getInstance().uid ?: ""

        // database = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, register_username_text_view.text.toString(),register_email_text_view.text.toString(),devices_code.text.toString() )

        ref.setValue(user)

                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Success save data to Firebase")

                    //open ConnectViaBluettooth when you success save data to database ***********
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to set value to database :${it.message}")
                }

    }
}
