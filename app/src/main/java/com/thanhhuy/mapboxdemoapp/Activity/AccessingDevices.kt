package com.thanhhuy.mapboxdemoapp.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thanhhuy.mapboxdemoapp.Class.User
import com.thanhhuy.mapboxdemoapp.R
import kotlinx.android.synthetic.main.activity_accessing_devices.*
import kotlinx.android.synthetic.main.activity_register.*

class AccessingDevices : AppCompatActivity() {

    internal var DevicesCode = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessing_devices)


        verifyUserIsLoggedIn()
        getDevicesCode()
        accessing_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Code",DevicesCode.toString() )
            Log.d("data","DevicesCode: $DevicesCode")
            startActivity(intent)
        }
    }


    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun getDevicesCode() {
        val userId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
               val data = p0.getValue(User::class.java)
                DevicesCode = data?.devices_code.toString()
                Log.d("data", "concac+ ${data?.devices_code}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
