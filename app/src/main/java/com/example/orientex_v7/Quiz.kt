package com.example.orientex_v7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class Quiz : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var id: String
    private var currentQuest: Int = 0
    private val size = 3
    private lateinit var answers: Array<String>
    private val db = Firebase.firestore
    private var score: Double = 0.00
    private val passingScore: Double = 100.00
    private val totalQs: Int = 3
    private var passedQuiz = false

    private lateinit var group1: RadioGroup
    private lateinit var group2: RadioGroup
    private lateinit var group3: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        email = intent.getStringExtra("Email").toString()
        currentQuest = intent.getIntExtra("CurrentQuest", 0).toString().toInt()
        id = intent.getStringExtra("ID").toString()

        answers = Array<String>(size) {"N/A"}
        for(i in 0 until size) { answers[i] = "N/A" }

        setup()

        val button = findViewById<Button>(R.id.quizButton)
        button.setOnClickListener { results() }

        //Log.i("AnswerCheck", "Running checkAnswer: ${checkAnswer(2,"349")}")
    }

    private fun setup() {
        setAnswer(0,"Hal Marcus Computer Science and Engineering")
        setAnswer(1,"Fourth")
        setAnswer(2,"349")

        group1 = findViewById<RadioGroup>(R.id.questionGroup1)
        group2 = findViewById<RadioGroup>(R.id.questionGroup2)
        group3 = findViewById<RadioGroup>(R.id.questionGroup3)

        /*
        GlobalScope.launch {
            runBlocking { getAnswers().await() }
        }*/
    }

    //scores & sees whether or not they pass
    private fun results() {

        score = 0.0

        val selected1 = findViewById<RadioButton>(group1.checkedRadioButtonId)
        val selected2 = findViewById<RadioButton>(group2.checkedRadioButtonId)
        val selected3 = findViewById<RadioButton>(group3.checkedRadioButtonId)

        if(checkAnswer(0, selected1.text as String)) { score += 100.0/totalQs }
        if(checkAnswer(1, selected2.text as String)) { score += 100.0/totalQs }
        if(checkAnswer(2, selected3.text as String)) { score += 100.0/totalQs }

        Log.i("AnswerChecked", "Selected answers: ${selected1.text}, ${selected2.text}, ${selected3.text}\nScore: $score")

        val scoreStr = String.format("%.2f", score)
        val passStr = String.format("%.2f", passingScore)
        val message = "Passing Score: $passStr\nYour Score: $scoreStr"

        Log.i("AnswerChecked", message)

        passedQuiz = score >= passingScore

        if(!passedQuiz) {
            Snackbar.make(findViewById(R.id.quizView), message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") {
                    selected1.isChecked = false
                    selected2.isChecked = false
                    selected3.isChecked = false
                }
                .show()
        }
        else {
            Snackbar.make(findViewById(R.id.quizView), message, Snackbar.LENGTH_INDEFINITE)
                .setAction("Continue") {  launchAct() }
                .show()

        }
    }

    //launching current quest after passing
    private fun launchAct() {
        val intent = Intent(this@Quiz, CurrentQuest::class.java)
        intent.putExtra("Email", email)
        intent.putExtra("ID", id)
        intent.putExtra("CurrentQuest", currentQuest)
        intent.putExtra("Passed", true)
        startActivity(intent)
    }

    //setting the array
    private fun setAnswer(index: Int, ans: String) {
        answers[index] = ans
        Log.i("AnswerCheck", "In setAnswer: ${answers[index]}")
    }

    //future build: would have it pulling answers from database
    private fun getAnswers() = GlobalScope.async {
        db.collection("Quiz Answers")
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    //Log.i("AnswerCheck", "Document: $document")

                    when(document.id) {
                        "Q1" -> {
                            //Log.i("AnswerCheck", "In Q1 -> ${document.get("answer").toString()}")
                            setAnswer(0,document.get("answer").toString())
                        }
                        "Q2" -> { setAnswer(1,document.get("answer").toString()) }
                        "Q3" -> { setAnswer(2,document.get("answer").toString()) }
                    }
                }
            }
            .addOnFailureListener {
                Log.i("AnswerCheck", "You Reached Failure Listener")
            }
    }

    //is the answer they picked correct?
    private fun checkAnswer(index: Int, ans: String): Boolean { return answers[index] == ans }


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
                intent.putExtra("Email", email)
                intent.putExtra("ID", id)
                intent.putExtra("CurrentQuest", currentQuest)
                startActivity(intent)
            }
            R.id.navigation_profile -> {
                val intent = Intent(this@Quiz, Profile::class.java)
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