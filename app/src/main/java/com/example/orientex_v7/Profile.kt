package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

class Profile : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var id: String
    private var currentQuest: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        email = intent.getStringExtra("Email").toString()
        currentQuest = intent.getIntExtra("CurrentQuest", 0).toString().toInt()
        id = intent.getStringExtra("ID").toString()

        val username = findViewById<TextView>(R.id.emailText)
        username.text = email

        val pic = findViewById<ImageView>(R.id.profilePicture)
        pic.setImageResource(R.drawable.uwf_vert_logo)

        val count = findViewById<TextView>(R.id.completedScore)
        count.text = currentQuest.toString()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(this@Profile, CurrentQuest::class.java)
                intent.putExtra("Email", email)
                intent.putExtra("ID", id)
                intent.putExtra("CurrentQuest", currentQuest)
                startActivity(intent)
            }
            R.id.navigation_quests -> {
                val intent = Intent(this@Profile, QuestList::class.java)
                intent.putExtra("Email", email)
                intent.putExtra("ID", id)
                intent.putExtra("CurrentQuest", currentQuest)
                startActivity(intent)
            }
        }

        return when (item.itemId) {
            R.id.navigation_home -> true
            R.id.navigation_quests -> true
            R.id.navigation_profile -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}