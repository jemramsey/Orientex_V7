package com.example.orientex_v7

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.orientex_v7.databinding.ActivityMainBinding
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var oneTapClient: SignInClient? = null
    private var signUpRequest: BeginSignInRequest? = null
    private var signInRequest: BeginSignInRequest? = null
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore


    private val oneTapResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result ->
        try {
            val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
            val idToken = credential?.googleIdToken
            when {
                idToken != null -> {
                    // Got an ID token from Google. Use it to authenticate
                    // with your backend.
                    val msg = "idToken: $idToken"
                    authenticateWithFirebase(idToken)
//                    Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE).show()
                   // Log.d("one tap", msg)
                }
                else -> {
                    // Shouldn't happen.
                    Log.d("one tap", "No ID token!")
//                    Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    Log.d("one tap", "One-tap dialog was closed.")
                    // Don't re-prompt the user.
//                    Snackbar.make(binding.root, "One-tap dialog was closed.", Snackbar.LENGTH_INDEFINITE).show()
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d("one tap", "One-tap encountered a network error.")
                    // Try again or just ignore.
//                    Snackbar.make(binding.root, "One-tap encountered a network error.", Snackbar.LENGTH_INDEFINITE).show()
                }
                else -> {
                    Log.d("one tap", "Couldn't get credential from result." +
                            " (${e.localizedMessage})")
//                    Snackbar.make(binding.root, "Couldn't get credential from result.\" +\n" +
//                            " (${e.localizedMessage})", Snackbar.LENGTH_INDEFINITE).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        binding.btnSignIn.setOnClickListener {
            displaySignIn()
        }

    }

    private fun displaySignIn(){
        oneTapClient?.beginSignIn(signInRequest!!)
            ?.addOnSuccessListener(this) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.d("btn click", e.localizedMessage!!)
            }
    }

    private fun displaySignUp() {
        oneTapClient?.beginSignIn(signUpRequest!!)
            ?.addOnSuccessListener(this) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp()
                Log.d("btn click", e.localizedMessage!!)
            }
    }

    //launch the rest of the app after login
    private fun updateUi(email : String, id : String, currentQuest: Int) {
        val intent = Intent(this@MainActivity, CurrentQuest::class.java)
        intent.putExtra("Email", email)
        intent.putExtra("ID", id)
        intent.putExtra("CurrentQuest", currentQuest)

        Log.i("UpdateUI", "User's Info: $email, $id, $currentQuest")

        startActivity(intent)
    }

    private fun authenticateWithFirebase(token: String?) {
        Log.i("AUTHCHECK", "$token")


        val firebaseCredential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(firebaseCredential)
        val user = auth.currentUser
        if (user != null) {
            Log.i("AUTHCHECK", user.email.toString())
            val email = user.email.toString()
            val name = user.displayName.toString()
            GlobalScope.launch {
                userQuery(name, email).await()
            }
        }
    }

    //searching for UID in database based on email
    private fun userQuery(name: String , email: String) = GlobalScope.async {

         var id = "NA"
         var currQuest = 0

         db.collection("Users")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { result ->
                Log.i("AUTHCHECK-exists", result.documents.toString())
                if(result.documents.isEmpty())
                {
                    id = addUser(name, email)
                }
                else {
                    id = result.documents[0].id
                    val questCompleted = result.documents[0].data?.get("Quest Completed").toString().toInt()
                    if(questCompleted > 0) { currQuest = questCompleted }
                }
                updateUi(email, id, currQuest)
            }
            .addOnFailureListener { result ->
                Log.i("AUTHCHECK-exists", "Cannot query DB")
            }
    }

    //add user to database
    private fun addUser(name :String, email: String): String {

        var id = "NA"

        val userData = hashMapOf(
            "ID" to email,
            "Name" to name,
            "Email" to email,
            "Quest Completed" to 0
        )

        db.collection("Users")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                id = documentReference.id
//                updateUi(email)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        return id
    }

    //increment user's quest count when the quest is completed
    fun updateQuestsCompleted(currentQuest : Int, id : String) {
        db.collection("Users")
            .document(id)
            .update("Quest Completed", currentQuest)
    }

    //retained for future development
    private fun getFbUserId(email: String) : String {
        var DocId = ""
        db.collection("Users")
            .whereEqualTo("Email",  email)
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                    DocId = document.id.toString()
                   // Log.d("USER", "$DocId")
            }
        return DocId
    }

}