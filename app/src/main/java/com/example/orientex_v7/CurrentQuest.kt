package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser

class CurrentQuest : AppCompatActivity() {

    private var currQuest = 0
    private val main = MainActivity()
    //private val currentUser = main.getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_quest)

        //TODO: set to user's current quest
        //currQuest = 0


        //TODO: Get it so that the image, title, and desc. update to the current quest's info
        val quest_button = findViewById<Button>(R.id.questButton)

        quest_button.setOnClickListener {
            when(currQuest) {
                0 -> updateUI()
                1 -> updateUI()
            }
            //startActivityForResult(Intent(this@CurrentQuest, QR_Scanner::class.java))
        }

    }

    private fun updateUI() {
        currQuest++

        //button's function first
        if(currQuest > 1) {
            val qr_info: String? = scan()
        }

        //set the info in the layout to be what it is: title, description, image
        val title = findViewById<TextView>(R.id.questTitle)
        val image = findViewById<ImageView>(R.id.questImage)
        val desc = findViewById<TextView>(R.id.questDesc)

        when(currQuest) {
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
        }

    }

    private fun scan(): String? {
        //launch qr scanner
        startActivity(Intent(this@CurrentQuest, QRScanner::class.java))

        //get the info from the QR Scanner
        val qr = QRScanner()
        val info = qr.getCode()

        //check off that the info is right (right qr code)
        when(info) {
            "HMCSE-435" -> { Toast.makeText(this, "Yay", Toast.LENGTH_SHORT).show() }
            "HMCSE-349" -> {}
        }

        return info
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