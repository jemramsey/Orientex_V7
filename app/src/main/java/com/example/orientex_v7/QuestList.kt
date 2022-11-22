package com.example.orientex_v7

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView

class QuestList : AppCompatActivity() {

    //size based on # of quests available
    private val size = 5
    private var quests = BooleanArray(size)
    private lateinit var email: String
    private var currentQuest = 0
    private lateinit var id: String
    //private var completedQuests = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_list)

        email = intent.getStringExtra("Email").toString()
        currentQuest = intent.getIntExtra("CurrentQuest", 0)
        id = intent.getStringExtra("ID").toString()

        for (i in 0 until size) { quests[i] = i < currentQuest }
/*
        //if there isn't any completed quests
        if(currentQuest < 1) {
            for (i in 0 until size) { quests[i] = false }
            Log.d("QUESTSETTER", quests.toString())
        }
        else {
            when(currentQuest) {
                0 -> for (i in 0 until size) { quests[i] = false }
                1 -> for (i in 0 until size) { quests[i] = i < 1 }
            }
        }
*/
        for(i in 0 until size) { updateImage(i) }
    }

    private fun updateImage(quest: Int) {
        val id: Int = when (quest) {
            0 -> R.id.introCheck
            1 -> R.id.quest1Check
            2 -> R.id.quest2Check
            3 -> R.id.quest3Check
            4 -> R.id.quest4Check
            else -> -1
        }
        val image = findViewById<ImageView>(id)
        val on = android.R.drawable.checkbox_on_background
        val off = android.R.drawable.checkbox_off_background

        if(quests[quest]) { image.setImageResource(on) }
        else { image.setImageResource(off) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.navigation_home -> {
                val intent = Intent(this@QuestList, CurrentQuest::class.java)
                intent.putExtra("Email", email)
                intent.putExtra("ID", id)
                intent.putExtra("CurrentQuest", currentQuest)
                startActivity(intent)
            }
            R.id.navigation_profile -> {
                val intent = Intent(this@QuestList, Profile::class.java)
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