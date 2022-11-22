package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

class Profile : AppCompatActivity() {

    private var email: String = CurrentQuest.getUserEmail()
    private var completedQuests: Int = CurrentQuest.getCurrentQuest() - 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //TODO: Set completed quests via database grab of the # the user has finished

        val username = findViewById<TextView>(R.id.emailText)
        username.text = email

        val pic = findViewById<ImageView>(R.id.profilePicture)
        pic.setImageResource(R.drawable.uwf_vert_logo)

        val count = findViewById<TextView>(R.id.completedScore)
        count.text = completedQuests.toString()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.navigation_home -> startActivity(Intent(this@Profile, CurrentQuest::class.java))
            R.id.navigation_quests -> {
                val intent = Intent(this@Profile, QuestList::class.java)
                intent.putExtra("currentQuest", CurrentQuest.getCurrentQuest())
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