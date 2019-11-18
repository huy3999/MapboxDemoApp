package com.thanhhuy.mapboxdemoapp.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.thanhhuy.mapboxdemoapp.Class.User
import com.thanhhuy.mapboxdemoapp.R
import kotlinx.android.synthetic.main.activity_login_activitykotlin.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivitykotlin : AppCompatActivity() {

    //internal lateinit var DevicesCode: String
    internal var DevicesCode = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_activitykotlin)

        //register
        navigate_login_main_activity.setOnClickListener {
            val intent = Intent(this, BackgroundActivity::class.java)
            startActivity(intent)
        }


        login_button.setOnClickListener {
            val email = email_editted_login.text.toString()
            val password = password_editted_login.text.toString()

            Log.d("Login","Attempt login with email and password: $email")

            if(!email.isEmpty() && !password.isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {
                            if(it.isSuccessful) {

//                                verifyUserIsLoggedIn()
//                                getDevicesCode()
                                //DevicesCode = "00236C8D9AFF244E"
                                val intent = Intent(this,AccessingDevices::class.java)
//                                intent.putExtra("Code",DevicesCode )
                                startActivity(intent)
                                //Log.d("code","code: + $DevicesCode")
//                                Log.d("LoginActivity","Successfully to Login Account")
                            }
                            else {
                                Log.d("LoginActivity","Failed to Login Account")
                            }
                        }
            }
        }
    }

//
//
//    private fun verifyUserIsLoggedIn() {
//        val uid = FirebaseAuth.getInstance().uid
//        if (uid == null) {
//            val intent = Intent(this, RegisterActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//        }
//    }

//    private fun getDevicesCode() {
//        val userId = FirebaseAuth.getInstance().uid
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")
//
//        ref.addListenerForSingleValueEvent(object: ValueEventListener {
//
//            override fun onDataChange(p0: DataSnapshot) {
//                val data = p0.getValue(User::class.java)
//                DevicesCode = data?.devices_code.toString()
//                Log.d("data", "concac+ ${data?.devices_code}")
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//    }
}
