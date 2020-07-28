package com.psygon.tech.scholar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

abstract class ParentActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceID())

        auth = FirebaseAuth.getInstance()
    }

    abstract fun getLayoutResourceID(): Int
    abstract fun initAll()
}
