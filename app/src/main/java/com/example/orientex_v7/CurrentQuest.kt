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
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class CurrentQuest : AppCompatActivity() {
    companion object {
        private var currQuest = 0
        fun getCurrentQuest(): Int { return currQuest }
}

    private lateinit var qrCode: String
    private lateinit var currentUserID: String
    private lateinit var currentUserEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUserEmail = intent.getStringExtra("Email").toString()
        qrCode = intent.getStringExtra("QRCode").toString()
        currentUserID = intent.getStringExtra("ID").toString()
        currQuest = intent.getIntExtra("CurrentQuest", 0)

        Log.i("CurrentQuest-Found", currQuest.toString())

        setContentView(R.layout.activity_current_quest)

        updateUI()

        //when launched, if current quest is 2 or 3, set currQuest-- & call nextQuest()
        //if code is not "NO_CODE", check that it's the right code, then call nextQuest()
        if((currQuest == 2 || currQuest == 3) && (qrCode != "NO_CODE" && checkCode(qrCode))) {
            nextQuest()
        }

        val questButton = findViewById<Button>(R.id.questButton)
        questButton.setOnClickListener {
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

        //Log.i("UpdateUI", "Current Quest: $currQuest")

        val main = MainActivity()
        main.updateQuestsCompleted(currQuest, currentUserID)

        updateUI()
    }

    private fun updateUI() {
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
            4 -> { startActivity(Intent(this@CurrentQuest, Quiz::class.java)) }
            5 -> {
                title.text = getString(R.string.congratsTitle)
                //image.setImageResource(android.R.drawable.) //set this to some sort of celebratory picture
                desc.text = getString(R.string.congratsDesc)
                val button = findViewById<Button>(R.id.questButton)
                button.isVisible = false
            }
        }
    }

    private fun launchScanner() {
        val intent = Intent(this@CurrentQuest, QRScanner::class.java)
        intent.putExtra("User", currentUserEmail)
        intent.putExtra("ID", currentUserID)
        intent.putExtra("CurrentQuest", currQuest)
        startActivity(intent)
    }

    private fun checkCode(info: String): Boolean {

        //check off that the info is right (right qr code)
        var correctCode = false
        when(info) {
            "HMCSE-435" -> {
                Toast.makeText(this, "Yay", Toast.LENGTH_SHORT).show()
                if(currQuest == 2) { correctCode = true }
            }
            "HMCSE-349" -> {if(currQuest == 3) { correctCode = true }}
        }

        Log.i("CodeCheck", "Code: $correctCode, Scanned: $info")

        return correctCode
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.navigation_quests -> {
                val intent = Intent(this@CurrentQuest, QuestList::class.java)
                intent.putExtra("Email", currentUserEmail)
                intent.putExtra("ID", currentUserID)
                intent.putExtra("CurrentQuest", currQuest)
                startActivity(intent)
            }
            R.id.navigation_profile -> {
                val intent = Intent(this@CurrentQuest, Profile::class.java)
                intent.putExtra("Email", currentUserEmail)
                intent.putExtra("ID", currentUserID)
                intent.putExtra("CurrentQuest", currQuest)
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