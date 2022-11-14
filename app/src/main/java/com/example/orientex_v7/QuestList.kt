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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_list)

        //TODO: pull from database what is and isn't completed

        val curQuest = intent.getIntExtra("currentQuest", 0)
        //currentUser = intent.getStringExtra("User").toString()
        if(curQuest < 1) {
            for (i in 0 until size) { quests[i] = false }
            Log.d("QUESTSETTER", quests.toString())
        }

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
            R.id.navigation_home -> startActivity(Intent(this@QuestList, CurrentQuest::class.java))
            R.id.navigation_profile -> startActivity(Intent(this@QuestList, Profile::class.java))
        }

        return when (item.itemId) {
            R.id.navigation_home -> true
            R.id.navigation_quests -> true
            R.id.navigation_profile -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}