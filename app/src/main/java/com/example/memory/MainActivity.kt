package com.example.memory

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.view.View
import android.widget.TextView

import android.os.SystemClock
import android.widget.GridLayout
import android.widget.ImageView
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.EditText


class MainActivity : ComponentActivity() {
    private var userAnswer              : MutableList<Int> = mutableListOf()
    private var expectedAnswer          : MutableList<Int> = mutableListOf()
    private var startTime               : Long = 0L
    private var resultTime              : Long = 0L
    private var correctCount            : Long = 0L
    private val handler                 : Handler = Handler(Looper.getMainLooper())

    private lateinit var kartNextButton : Button
    private lateinit var sayiNextButton : Button
    private lateinit var kartButton     : Button
    private lateinit var sayiButton     : Button
    private lateinit var endTimeText    : TextView
    private lateinit var totalCorText   : TextView
    private lateinit var corSizeText    : TextView
    private lateinit var timeInput      : EditText
    private lateinit var gridLayout     : GridLayout


    private val defaultOrderCardsImgs = mutableListOf(R.drawable.clubs_a, R.drawable.clubs_2, R.drawable.clubs_3,
        R.drawable.clubs_4, R.drawable.clubs_5, R.drawable.clubs_6,
        R.drawable.clubs_7, R.drawable.clubs_8, R.drawable.clubs_9,
        R.drawable.clubs_10, R.drawable.clubs_j, R.drawable.clubs_q, R.drawable.clubs_k,
        R.drawable.diamonds_a, R.drawable.diamonds_2, R.drawable.diamonds_3,
        R.drawable.diamonds_4, R.drawable.diamonds_5, R.drawable.diamonds_6,
        R.drawable.diamonds_7, R.drawable.diamonds_8, R.drawable.diamonds_9,
        R.drawable.diamonds_10, R.drawable.diamonds_j, R.drawable.diamonds_q, R.drawable.diamonds_k,
        R.drawable.hearts_a, R.drawable.hearts_2, R.drawable.hearts_3,
        R.drawable.hearts_4, R.drawable.hearts_5, R.drawable.hearts_6,
        R.drawable.hearts_7, R.drawable.hearts_8, R.drawable.hearts_9,
        R.drawable.hearts_10, R.drawable.hearts_j, R.drawable.hearts_q, R.drawable.hearts_k,
        R.drawable.spades_a, R.drawable.spades_2, R.drawable.spades_3,
        R.drawable.spades_4, R.drawable.spades_5, R.drawable.spades_6,
        R.drawable.spades_7, R.drawable.spades_8, R.drawable.spades_9,
        R.drawable.spades_10, R.drawable.spades_j, R.drawable.spades_q, R.drawable.spades_k)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeVariables()
        addListenersToButtons()
    }

    private fun addListenersToButtons() {
        kartButton.setOnClickListener  {showKartPage()}
        sayiButton.setOnClickListener  {showSayiPage()}
        kartNextButton.setOnClickListener {
            resultTime = SystemClock.elapsedRealtime() - startTime
            kartNextButton.visibility = View.GONE
            showOrderCardsPage()
        }
    }
    fun generateRandomStrings(): List<String> {
        return List(52) {
            (1..3).map { ('0'..'9').random() }.joinToString("")
        }
    }

    private fun showNumbers() {
        val numbers = generateRandomStrings()
        for (number in numbers) {
            val textView = TextView(this).apply {
                textSize = 22f
                setPadding(16, 16, 16, 16)
                setBackgroundResource(android.R.color.darker_gray)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = GridLayout.LayoutParams.WRAP_CONTENT
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(16, 45, 16, 16)
                }
            }
            gridLayout.addView(textView)
        }
    }

    private fun showSayiPage() {
        val minute = timeInput.text.toString().toIntOrNull() ?: return
        performCommonsOfPages()
        sayiNextButton.visibility = View.VISIBLE

        handler.postDelayed({


        }, (minute * 60 * 1000).toLong())



    }

    private fun performCommonsOfPages() {
        hideInitialPage()
        startTime = SystemClock.elapsedRealtime()
        gridLayout.visibility = View.VISIBLE
        gridLayout.removeAllViews()
    }

    private fun hideInitialPage() {
        kartButton.visibility   = View.GONE
        sayiButton.visibility   = View.GONE
        endTimeText.visibility  = View.GONE
        totalCorText.visibility = View.GONE
        timeInput.visibility    = View.GONE
        corSizeText.visibility  = View.GONE
    }

    private fun showCards(cards: List<Int>, onCardClick: (cardResource: Int, imageView: ImageView) -> Unit = { _, _ -> }){
        gridLayout.removeAllViews()
        for (cardResource in cards) {
            val imageView = ImageView(this)
            imageView.setImageResource(cardResource)
            val params = GridLayout.LayoutParams()
            params.width = 150
            params.height = 198
            params.setMargins(8, 8, 8, 8)
            imageView.layoutParams = params
            imageView.setOnClickListener {
                onCardClick(cardResource, imageView)
            }
            gridLayout.addView(imageView)
        }
    }

    private fun showOrderCardsPage() {
        showCards(defaultOrderCardsImgs) { cardResource, imageView ->
            if (userAnswer.contains(cardResource)) {
                userAnswer.remove(cardResource)
                imageView.setBackgroundColor(Color.TRANSPARENT)
            } else {
                userAnswer.add(cardResource)
                imageView.setBackgroundColor(Color.GREEN)
            }
            if (userAnswer.size == 52){
                correctCount = getCorrectCount(expectedAnswer, userAnswer)
                showStartPage()
            }
        }
    }

    private fun getCorrectCount(currentList: MutableList<Int>, answerList: MutableList<Int>): Long {
        val minSize = minOf(currentList.size, answerList.size)
        var count = 0L
        for (i in 0 until minSize) {
            if (currentList[i] == answerList[i]) count++
        }
        return count
    }

    private fun showKartPage() {
        performCommonsOfPages()
        expectedAnswer = defaultOrderCardsImgs.toMutableList()
        expectedAnswer.shuffle()
        kartNextButton.visibility = View.VISIBLE
        showCards(expectedAnswer)
    }

    private fun hidePages() {
        userAnswer.clear()
        expectedAnswer.clear()
        gridLayout.visibility  = View.GONE
        gridLayout.visibility  = View.GONE
        sayiNextButton.visibility = View.GONE
    }

    private fun showTime() {
        endTimeText.visibility = View.VISIBLE
        val seconds = resultTime / 1000
        val milliseconds = resultTime % 1000
        endTimeText.text = "Total Time: ${seconds}s ${milliseconds}ms"
        startTime = 0L
    }

    private fun showCorrect() {
        totalCorText.visibility = View.VISIBLE
        totalCorText.text = "Total Correct: $correctCount"
    }

    private fun showSize() {
        corSizeText.visibility = View.VISIBLE
        corSizeText.text       = "Total Size: ${expectedAnswer.size}"
    }
    private fun showStartPage() {
        showSize()
        hidePages()
        showTime()
        showCorrect()
        kartButton.visibility = View.VISIBLE
        sayiButton.visibility = View.VISIBLE
        timeInput.visibility  = View.VISIBLE
    }

    private fun initializeVariables() {
        kartButton     = findViewById<Button>(R.id.button1)
        sayiButton     = findViewById<Button>(R.id.button2)
        sayiNextButton = findViewById<Button>(R.id.sayiNextButton)
        kartNextButton = findViewById<Button>(R.id.kartNextButton)
        endTimeText    = findViewById<TextView>(R.id.endTimeText)
        totalCorText   = findViewById<TextView>(R.id.totalCorrectText)
        corSizeText    = findViewById<TextView>(R.id.totalSizeText)
        timeInput      = findViewById<EditText>(R.id.timeInput)
        endTimeText    = findViewById<TextView>(R.id.endTimeText)
        gridLayout     = findViewById<GridLayout>(R.id.GridLayout)


        endTimeText.visibility    = View.GONE
        gridLayout.visibility     = View.GONE
        kartNextButton.visibility = View.GONE
        totalCorText.visibility   = View.GONE
        sayiNextButton.visibility = View.GONE
        corSizeText.visibility    = View.GONE
    }
}


