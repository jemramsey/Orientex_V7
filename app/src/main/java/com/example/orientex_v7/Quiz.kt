package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class Quiz : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        //TODO: Setup the handling of & submission of the quiz
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //TODO: Setup navigation to the others
        when(item.itemId) {
            R.id.navigation_quests -> {
                val intent = Intent(this@Quiz, QuestList::class.java)
                intent.putExtra("currentQuest", CurrentQuest.getCurrentQuest())
                startActivity(intent)
            }
            R.id.navigation_profile -> startActivity(Intent(this@Quiz, Profile::class.java))
        }

        return when (item.itemId) {
            R.id.navigation_home -> true
            R.id.navigation_quests -> true
            R.id.navigation_profile -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}