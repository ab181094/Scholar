package com.psygon.tech.scholar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.psygon.tech.scholar.activities.HomeActivity
import com.psygon.tech.scholar.activities.ParentActivity
import com.psygon.tech.scholar.helpers.*
import com.psygon.tech.scholar.models.Users

class MainActivity : ParentActivity(), View.OnClickListener {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInButton: SignInButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAll()
        subscribeUser()
        loadPredefinedSuggestedWords(this)
    }

    private fun subscribeUser() {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GENERAL)
            .addOnCompleteListener { task ->
                var msg = resources.getString(R.string.subscribe_successful)
                if (!task.isSuccessful) {
                    msg = resources.getString(R.string.subscribe_failed)
                }

                log(msg)
            }
    }

    override fun getLayoutResourceID(): Int {
        return R.layout.activity_main
    }

    override fun initAll() {
        progressBar = findViewById(R.id.progressBar)
        hideProgressBar(progressBar)

        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.isEnabled = false

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            signInButton.id -> {
                signInButton.isEnabled = false
                signIn()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                log(e.message)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        showProgressBar(progressBar)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUser(acct)
                } else {
                    toast(this, AUTHENTICATION_FAILED_MSG)
                    signInButton.isEnabled = true
                }

                hideProgressBar(progressBar)
            }
    }

    private fun addUser(acct: GoogleSignInAccount) {
        val user = Users()

        user.name = acct.displayName!!
        user.email = acct.email!!
        user.key = encodeEmail(user.email)

        database = FirebaseDatabase.getInstance().getReference(USER_PATH)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChild(user.key)) {
                    appUser = p0.child(user.key).getValue(Users::class.java)!!

                    if (appUser.isBlackListed) {
                        showMessageDialog(this@MainActivity, USER_BLACKLISTED_MSG)
                    } else {
                        navigateUser(auth.currentUser)
                    }
                } else {
                    database.child(user.key).setValue(user)

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(acct.displayName)
                        .setPhotoUri(acct.photoUrl)
                        .build()

                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                navigateUser(auth.currentUser)
                            }
                        }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val key = encodeEmail(currentUser.email!!)

            database = FirebaseDatabase.getInstance().getReference(USER_PATH)
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.hasChild(key)) {
                        appUser = p0.child(key).getValue(Users::class.java)!!

                        if (appUser.isBlackListed) {
                            showMessageDialog(this@MainActivity, USER_BLACKLISTED_MSG)
                        } else {
                            navigateUser(currentUser)
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        } else {
            signInButton.isEnabled = true
        }
    }

    private fun navigateUser(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            showMessageDialog(this, "$WELCOME_MSG ${currentUser.displayName}")

            Handler().postDelayed({
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()

            }, 1500)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
