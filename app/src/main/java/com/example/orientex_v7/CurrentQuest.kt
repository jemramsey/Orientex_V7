package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser

class CurrentQuest : AppCompatActivity() {
    companion object {
        var currQuest = 0
    }

    //private var currQuest = 0
    private val main = MainActivity()
    private lateinit var currentUser: String
    private lateinit var qrCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUser = intent.getStringExtra("User").toString()
        qrCode = intent.getStringExtra("QRCode").toString()

        Log.d("QRCODE", qrCode)

        //TODO: set to user's current quest & fix view to match
        setContentView(R.layout.activity_current_quest)
        //currQuest = 0

        //when launched, if current quest is 2 or 3, set currQuest-- & call nextQuest()
        //if code is not "NO_CODE", check that it's the right code, then call nextQuest()
        if(currQuest == 2 || currQuest == 3) {
            currQuest--
            nextQuest()

            if(qrCode != null && qrCode != "NO_CODE" && checkCode(qrCode)) {
                nextQuest()
            }
        }






        Log.d("EMAILCHECK", currentUser)

        //TODO: Get it so that the image, title, and desc. update to the current quest's info
        val quest_button = findViewById<Button>(R.id.questButton)

        quest_button.setOnClickListener {
            when(currQuest) {
                0 -> nextQuest()
                1 -> nextQuest()
                2 -> launchScanner()
                3 -> launchScanner()
                4 -> nextQuest()
            }
            //startActivityForResult(Intent(this@CurrentQuest, QR_Scanner::class.java))
        }

    }

    private fun nextQuest() {
        currQuest++

        //set the info in the layout to be what it is: title, description, image
        val title = findViewById<TextView>(R.id.questTitle)
        val image = findViewById<ImageView>(R.id.questImage)
        val desc = findViewById<TextView>(R.id.questDesc)

        when(currQuest) {
            0 -> {
                title.text = getString(R.string.introTitle)
                image.setImageResource(R.drawable.uwf_horizontal_logo)
                desc.text = getString(R.string.introDesc)
            }
            1 -> {
                title.text = getString(R.string.quest1Title)
                image.setImageResource(R.drawable.building_4_hmcse)
                desc.text = getString(R.string.quest1Desc)
            }
            2 -> {
                title.text = getString(R.string.quest2Title)
                image.setImageResource(R.drawable.room_tag)
                desc.text = getString(R.string.quest2Desc)
            }
            3 -> {
                title.text = getString(R.string.quest3Title)
                image.setImageResource(R.drawable.office_tag)
                desc.text = getString(R.string.quest3Desc)
            }
            //4 -> { startActivity(Quiz) }
        }

    }

    private fun launchScanner() { startActivity(Intent(this@CurrentQuest, QRScanner::class.java)) }

    private fun checkCode(info: String): Boolean {
        //get the info from the QR Scanner
        //val qr = QRScanner()
        //val info = qr.getCode()

        //check off that the info is right (right qr code)
        var correctCode = false
        when(info) {
            "HMCSE-435" -> {
                Toast.makeText(this, "Yay", Toast.LENGTH_SHORT).show()
                if(currQuest == 2) { correctCode = true }
            }
            "HMCSE-349" -> {if(currQuest == 3) { correctCode = true }}
        }

        return correctCode
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.navigation_home -> setContentView(R.layout.activity_current_quest)
            R.id.navigation_quests -> setContentView(R.layout.activity_quest_list)
        }

        return when (item.itemId) {
            R.id.navigation_home -> true
            R.id.navigation_quests -> true
            R.id.navigation_profile -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}