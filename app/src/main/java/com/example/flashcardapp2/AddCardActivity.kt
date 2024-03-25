package com.example.flashcardapp2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class AddCardActivity : AppCompatActivity() {

    private val ADD_CARD_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_card)

        // Récupération des données de l'Intent
        val question = intent.getStringExtra("question")
        val answer1 = intent.getStringExtra("answer1")
        val answer2 = intent.getStringExtra("answer2")
        val answer3 = intent.getStringExtra("answer3")


        val questionText = findViewById<TextView>(R.id.editQuestion)
        val answerText1 = findViewById<TextView>(R.id.editAnswer1)
        val answerText2 = findViewById<TextView>(R.id.editAnswer2)
        val answerText3 = findViewById<TextView>(R.id.editAnswer3)

        val closeBtn = findViewById<ImageView>(R.id.closeBtn)
        val saveBtn = findViewById<ImageView>(R.id.saveBtn)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        saveBtn.setOnClickListener {
            // Retrieve user input
            val question = questionText.text.toString()
            val answer1 = answerText1.text.toString()
            val answer2 = answerText2.text.toString()
            val answer3 = answerText3.text.toString()

            val toast = Toast.makeText(this, "field all the line !!!", Toast.LENGTH_SHORT);

            if (answer1.isEmpty() || answer2.isEmpty() ||answer3.isEmpty() || question.isEmpty()) {
                // Show a toast message if one of them
                toast.show()
            }
            else {
                // Create an Intent to pass data back to MainActivity
                val resultIntent = Intent().apply {
                    putExtra("question", question)
                    putExtra("answer1", answer1)
                    putExtra("answer2", answer2)
                    putExtra("answer3", answer3)

                }
                setResult(Activity.RESULT_OK, resultIntent)

                // Finish AddCardActivity
                finish()
            }
        }


        closeBtn.setOnClickListener {
            // Finish AddCardActivity
            finish()
        }

        questionText.setText(question)
        answerText1.setText(answer1)
        answerText2.setText(answer2)
        answerText3.setText(answer3)

    }


}