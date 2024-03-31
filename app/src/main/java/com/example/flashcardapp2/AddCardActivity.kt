package com.example.flashcardapp2


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddCardActivity : AppCompatActivity() {

    private lateinit var flashcardDatabase: FlashcardDatabase
    private var isUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        flashcardDatabase = FlashcardDatabase(this)

        val btnSave = findViewById<View>(R.id.saveBtn)
        val btnClose = findViewById<View>(R.id.closeBtn)

        isUpdate = intent.getBooleanExtra("isUpdate", false)
        if (isUpdate) {
            // Si c'est une mise à jour, remplir les champs avec les données de la carte actuelle
            fillFieldsWithFlashcardData()
        }

        btnSave.setOnClickListener {
            saveFlashcard()
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun fillFieldsWithFlashcardData() {
        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer1")
        val wrongAnswer1 = intent.getStringExtra("answer2")
        val wrongAnswer2 = intent.getStringExtra("answer3")

        findViewById<EditText>(R.id.editQuestion).setText(question)
        findViewById<EditText>(R.id.editAnswer1).setText(answer)
        findViewById<EditText>(R.id.editAnswer2).setText(wrongAnswer1)
        findViewById<EditText>(R.id.editAnswer3).setText(wrongAnswer2)
    }

    private fun saveFlashcard() {
        val editQuestion = findViewById<EditText>(R.id.editQuestion).text.toString()
        val editAnswer1 = findViewById<EditText>(R.id.editAnswer1).text.toString()
        val editAnswer2 = findViewById<EditText>(R.id.editAnswer2).text.toString()
        val editAnswer3 = findViewById<EditText>(R.id.editAnswer3).text.toString()
        val uuid = intent.getIntExtra("uuid", -1)

        if (editQuestion.isEmpty() || editAnswer1.isEmpty() || editAnswer2.isEmpty() || editAnswer3.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val newFlashcard = Flashcard(editQuestion, editAnswer1, editAnswer2, editAnswer3)

        if (isUpdate && uuid != -1) {
            // Mise à jour de la carte existante
            newFlashcard.uuid = uuid
            flashcardDatabase.updateCard(newFlashcard)
            Toast.makeText(this, "Successfully Update.", Toast.LENGTH_SHORT).show()
        } else {
            // Ajout d' une nouvelle carte
            flashcardDatabase.insertCard(newFlashcard)
            Toast.makeText(this, "Successfully Create.", Toast.LENGTH_SHORT).show()
        }
        // Retourner le résultat à l'activité appelante
        setResult(Activity.RESULT_OK)

        // Redémarrer MainActivity
        startActivity(Intent(this, MainActivity::class.java))

        finish()
    }
}
