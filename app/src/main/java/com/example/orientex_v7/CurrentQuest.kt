package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible


class CurrentQuest : AppCompatActivity() {
    companion object {
        private var currQuest = 0
        fun getCurrentQuest(): Int { return currQuest }
}

    private lateinit var qrCode: String
    private lateinit var currentUserID: String
    private lateinit var currentUserEmail: String
    private var passedQuiz: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentUserEmail = intent.getStringExtra("Email").toString()
        qrCode = intent.getStringExtra("QRCode").toString()
        currentUserID = intent.getStringExtra("ID").toString()
        currQuest = intent.getIntExtra("CurrentQuest", 0)
        passedQuiz = intent.getBooleanExtra("Passed", false)

        Log.i("CurrentQuest-Found", currQuest.toString())

        setContentView(R.layout.activity_current_quest)

        if(passedQuiz) { nextQuest() }

        //when launched, if current quest is 2 or 3, set currQuest-- & call nextQuest()
        //if code is not "NO_CODE", check that it's the right code, then call nextQuest()
        if((currQuest == 2 || currQuest == 3) && (qrCode != "NO_CODE" && checkCode(qrCode))) {
            nextQuest()
        }

        Log.i("CurrentQuest-F", "Current Quest: $currQuest, Passed: $passedQuiz")

        updateUI()

        val questButton = findViewById<Button>(R.id.questButton)
        questButton.setOnClickListener {
            when(currQuest) {
                0 -> nextQuest()
                1 -> nextQuest()
                2 -> launchAct("QR")
                3 -> launchAct("QR")
                //4 -> nextQuest()
            }
            //startActivityForResult(Intent(this@CurrentQuest, QR_Scanner::class.java))
        }

    }

    //update the current quest
    private fun nextQuest() {
        currQuest++

        Log.i("CurrentQuest-F", "Current Quest: $currQuest")

        val main = MainActivity()
        main.updateQuestsCompleted(currQuest, currentUserID)

        updateUI()
    }

    //change current quest's UI to match current quest
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
            4 -> {
                launchAct("Quiz")
            }
            else -> {
                title.text = getString(R.string.congratsTitle)
                //image.setImageResource(android.R.drawable.) //set this to some sort of celebratory picture
                desc.text = getString(R.string.congratsDesc)
                val button = findViewById<Button>(R.id.questButton)
                button.isVisible = false
            }
        }
    }

    //launch either QR or Quiz
    private fun launchAct(act: String) {
        val intent = when(act) {
            "QR" -> Intent(this@CurrentQuest, QRScanner::class.java)
            "Quiz" -> Intent(this@CurrentQuest, Quiz::class.java)
            else -> {Intent(this@CurrentQuest, CurrentQuest::class.java)}
        }
        intent.putExtra("Email", currentUserEmail)
        intent.putExtra("ID", currentUserID)
        intent.putExtra("CurrentQuest", currQuest)
        startActivity(intent)
    }

    //check QR code for the correct one
    private fun checkCode(info: String): Boolean {

        //check off that the info is right (right qr code)
        var correctCode = false
        when(info) {
            "HMCSE-435" -> {
                //Toast.makeText(this, "Yay", Toast.LENGTH_SHORT).show()
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