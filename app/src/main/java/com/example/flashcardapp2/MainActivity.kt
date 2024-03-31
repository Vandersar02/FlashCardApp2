package com.example.flashcardapp2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private val addRequest = 1
    private val editRequest = 2
    private var currentFlashcardIndex = 0
    private var allFlashcards = mutableListOf<Flashcard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisez votre base de données et récupérez les flashcards
        val flashcardDatabase = FlashcardDatabase(this)
        flashcardDatabase.initFirstCard()
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        // afficher tous les élements de la base de données
        displayFlashcard(currentFlashcardIndex)

        val parentLayout = findViewById<View>(R.id.parentLayout)

        val questionText = findViewById<TextView>(R.id.question_txt)
        val answerText1 = findViewById<TextView>(R.id.answer1_txt)
        val answerText2 = findViewById<TextView>(R.id.answer2_txt)
        val answerText3 = findViewById<TextView>(R.id.answer3_txt)

        val plusBtn = findViewById<View>(R.id.plusBtn)
        val editBtn = findViewById<View>(R.id.editBtn)
        val btnNext = findViewById<View>(R.id.btnNext)
        val btnDelete = findViewById<View>(R.id.btnDelete)

        answerText1.setOnClickListener {
            if((answerText1.text).isNotEmpty()){
                answerText1.setBackgroundColor(Color.GREEN)
                answerText2.isEnabled = false
                answerText3.isEnabled = false
            }

        }

        answerText2.setOnClickListener {
            if((answerText2.text).isNotEmpty()){
                answerText1.setBackgroundColor(Color.GREEN)
                answerText2.setBackgroundColor(Color.RED)
                answerText1.isEnabled = false
                answerText3.isEnabled = false
            }

        }

        answerText3.setOnClickListener {
            if((answerText3.text).isNotEmpty()){
                answerText1.setBackgroundColor(Color.GREEN)
                answerText3.setBackgroundColor(Color.RED)
                answerText1.isEnabled = false
                answerText2.isEnabled = false
            }

        }

        parentLayout.setOnClickListener(View.OnClickListener { // Reset colors of the TextViews
            answerText1.setBackgroundColor(getResources().getColor(R.color.grown))
            answerText2.setBackgroundColor(getResources().getColor(R.color.grown))
            answerText3.setBackgroundColor(getResources().getColor(R.color.grown))
            answerText1.isEnabled = true
            answerText2.isEnabled = true
            answerText3.isEnabled = true
        })

        plusBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, AddCardActivity::class.java)
            startActivityForResult(intent, addRequest)
        }

        editBtn.setOnClickListener {
            if (allFlashcards.isNotEmpty()) {
                val (question, answer, wrongAnswer1, wrongAnswer2, uuid) = allFlashcards[currentFlashcardIndex]

                val intent = Intent(this@MainActivity, AddCardActivity::class.java)
                intent.putExtra("isUpdate", true)
                intent.putExtra("uuid", uuid) // Passer l'UUID de la carte à éditer
                intent.putExtra("question", question)
                intent.putExtra("answer1", answer)
                intent.putExtra("answer2", wrongAnswer1)
                intent.putExtra("answer3", wrongAnswer2)
                startActivityForResult(intent, editRequest)
            } else {
                Snackbar.make(findViewById(android.R.id.content), "No card to edit.", Snackbar.LENGTH_LONG).show()
            }
        }

        btnNext.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                // return here, so that the rest of the code in this onClickListener doesn't execute
                return@setOnClickListener
            }
            // Afficher la carte suivante
            currentFlashcardIndex =getRandomNumber(maxNumber = allFlashcards.size-1)
            Log.i("MainActivity","Current index: $currentFlashcardIndex")

            if (currentFlashcardIndex >= allFlashcards.size) {
                currentFlashcardIndex = 0 // Revenir à la première carte si nous avons atteint la fin
                Snackbar.make(findViewById(android.R.id.content), "Back to first question.", Snackbar.LENGTH_LONG).show()
            }
            if (currentFlashcardIndex == allFlashcards.size-1 && allFlashcards.size == 1){
                Snackbar.make(findViewById(android.R.id.content), "No more questions.", Snackbar.LENGTH_LONG).show()
            }
            displayFlashcard(currentFlashcardIndex)
        }

        btnDelete.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                // return here, so that the rest of the code in this onClickListener doesn't execute
                return@setOnClickListener
            }

            val (question) = allFlashcards[currentFlashcardIndex]

            // Supprimer la carte actuelle
            if (allFlashcards.size > 1) {
                flashcardDatabase.deleteCard(question)
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()

                if (currentFlashcardIndex >= allFlashcards.size) {
                    currentFlashcardIndex = 0 // Revenir à la première carte si nous avons supprimé la dernière
                }

                displayFlashcard(currentFlashcardIndex)
            }
            else if(allFlashcards.size == 1){
                flashcardDatabase.deleteCard(question)
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                questionText.text = ""
                answerText1.text = ""
                answerText2.text = ""
                answerText3.text = ""
                displayFlashcard(currentFlashcardIndex)
            }
            else{
                displayFlashcard(currentFlashcardIndex)
            }

        }

    }

    private fun displayFlashcard(index: Int) {
        if (allFlashcards.isNotEmpty()) {
            val (question, answer, wrongAnswer1, wrongAnswer2) = allFlashcards[index]

            val questionText = findViewById<TextView>(R.id.question_txt)
            val answerText1 = findViewById<TextView>(R.id.answer1_txt)
            val answerText2 = findViewById<TextView>(R.id.answer2_txt)
            val answerText3 = findViewById<TextView>(R.id.answer3_txt)

            questionText.text = question
            answerText1.text = answer
            answerText2.text = wrongAnswer1
            answerText3.text = wrongAnswer2
        } else {
            // Afficher un Snackbar indiquant que la base de données est vide
            Snackbar.make(findViewById(android.R.id.content), "Database is empty", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun getRandomNumber(minNumber: Int =0, maxNumber: Int): Int {
        return (minNumber..maxNumber).random()
    }

}
