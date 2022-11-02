package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

class CurrentQuest : AppCompatActivity() {

    private lateinit var currQuest: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_quest)

        //TODO: Get it so that the image, title, and desc. update to the current quest's info


        val quest_button = findViewById<Button>(R.id.questButton)

        quest_button.setOnClickListener {
            when(currQuest) {
                "quest1" -> updateUI(R.layout.activity_current_quest)
            }
            //startActivityForResult(Intent(this@CurrentQuest, QR_Scanner::class.java))
        }

    }

    private fun updateUI(layout: Int) {
        //set the info in the layout to be what it is: title, description, image
        //val title =
    }

    private fun scan() {
        //launch qr scanner

        //get the info from the QR Scanner

        //check off that the info is right (right qr code)

        //
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