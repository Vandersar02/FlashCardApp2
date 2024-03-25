package com.example.flashcardapp2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val ADD_CARD_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val questionText = findViewById<TextView>(R.id.question_txt)
        val answerText1 = findViewById<TextView>(R.id.answer1_txt)
        val answerText2 = findViewById<TextView>(R.id.answer2_txt)
        val answerText3 = findViewById<TextView>(R.id.answer3_txt)
        val plusBtn = findViewById<View>(R.id.plusBtn)
        val editBtn = findViewById<View>(R.id.editBtn)

        plusBtn.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivityForResult(intent, ADD_CARD_REQUEST)
        }

        editBtn.setOnClickListener {
            val question = questionText.text.toString()
            val answer1 = answerText1.text.toString()
            val answer2 = answerText2.text.toString()
            val answer3 = answerText3.text.toString()

            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("question", question)
            intent.putExtra("answer1", answer1)
            intent.putExtra("answer2", answer2)
            intent.putExtra("answer3", answer3)
            startActivityForResult(intent, ADD_CARD_REQUEST)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_CARD_REQUEST && resultCode == Activity.RESULT_OK) {
            val question = data?.getStringExtra("question")
            val answer1 = data?.getStringExtra("answer1")
            val answer2 = data?.getStringExtra("answer2")
            val answer3 = data?.getStringExtra("answer3")

            // Update the TextViews with the received data
            findViewById<TextView>(R.id.question_txt).text = question
            findViewById<TextView>(R.id.answer1_txt).text = answer1
            findViewById<TextView>(R.id.answer2_txt).text = answer2
            findViewById<TextView>(R.id.answer3_txt).text = answer3


            // Get the root view of the layout
            val rootView = findViewById<View>(android.R.id.content)

            // Show the snackbar message for the new card
            Snackbar.make(rootView, "The new card is created successfully", Snackbar.LENGTH_SHORT).show()


        }
    }

}
