package com.example.memory

import android.opengl.Visibility
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.view.View
import android.widget.TextView

import android.os.SystemClock
import android.widget.GridLayout
import android.widget.ImageView
import android.graphics.Color
import org.w3c.dom.Text


class MainActivity : ComponentActivity() {
    private var selectedCards           : MutableList<Int> = mutableListOf()
    private var startTime               : Long = 0L
    private var resultTime              : Long = 0L
    private var wrongCount              : Long = 0L
    private lateinit var kartButton     : Button
    private lateinit var sayiButton     : Button
    private lateinit var fnishButton    : Button
    private lateinit var endTimeText    : TextView
    private lateinit var totalWrongText : TextView

    private lateinit var gridLayout     : GridLayout
    private lateinit var kartNextButton : Button


    private val cardImgs = mutableListOf(
        R.drawable.clubs_a, R.drawable.clubs_2, R.drawable.clubs_3,
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
        R.drawable.spades_10, R.drawable.spades_j, R.drawable.spades_q, R.drawable.spades_k
    )

    private val defaultOrderCardsImgs = cardImgs.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeVariables()
        addListenersToButtons()
    }

    private fun addListenersToButtons() {
        kartButton.setOnClickListener  {showKartPage()}
        sayiButton.setOnClickListener  {showSayiPage()}
        fnishButton.setOnClickListener {showStartPage()}
        kartNextButton.setOnClickListener {
            resultTime = SystemClock.elapsedRealtime() - startTime
            kartNextButton.visibility = View.GONE
            showOrderCardsPage()
        }
    }

    private fun showSayiPage() {
        performCommonsOfPages()
    }

    private fun performCommonsOfPages() {
        hideInitialPage()
        startTime = SystemClock.elapsedRealtime()
    }

    private fun hideInitialPage() {
        kartButton.visibility  = View.GONE
        sayiButton.visibility  = View.GONE
        endTimeText.visibility = View.GONE
        totalWrongText.visibility = View.GONE
    }

    private fun showCards(cards: List<Int>, onCardClick: (cardResource: Int, imageView: ImageView) -> Unit = { _, _ -> }){
        gridLayout.removeAllViews()
        gridLayout.visibility = View.VISIBLE
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
            if (selectedCards.contains(cardResource)) {
                selectedCards.remove(cardResource)
                imageView.setBackgroundColor(Color.TRANSPARENT)
            } else {
                selectedCards.add(cardResource)
                imageView.setBackgroundColor(Color.GREEN)
            }
            if (selectedCards.size == 52){
                wrongCount = getDiff(cardImgs, selectedCards)
                showStartPage()
            }
        }
    }

    private fun getDiff(currentList: MutableList<Int>, answerList: MutableList<Int>): Long {
        val minSize = minOf(currentList.size, answerList.size)
        var count = 0L
        for (i in 0 until minSize) {
            if (currentList[i] != answerList[i]) count++
        }
        return count
    }
    
    private fun showKartPage() {
        performCommonsOfPages()
        cardImgs.shuffle()
        kartNextButton.visibility = View.VISIBLE
        showCards(cardImgs)
    }

    private fun hidePages() {
        selectedCards.clear()
        gridLayout.visibility  = View.GONE
        fnishButton.visibility = View.GONE
        gridLayout.visibility  = View.GONE
    }

    private fun showTime() {
        endTimeText.visibility = View.VISIBLE
        val seconds = resultTime / 1000
        val milliseconds = resultTime % 1000
        endTimeText.text = "Total Time: ${seconds}s ${milliseconds}ms"
        startTime = 0L
    }

    private fun showWrong() {
        totalWrongText.visibility = View.VISIBLE
        totalWrongText.text = "Total Wrong: $wrongCount"
    }

    private fun showStartPage() {
        hidePages()
        showTime()
        showWrong()
        kartButton.visibility = View.VISIBLE
        sayiButton.visibility = View.VISIBLE


    }

    private fun initializeVariables() {
        kartButton     = findViewById<Button>(R.id.button1)
        sayiButton     = findViewById<Button>(R.id.button2)
        fnishButton    = findViewById<Button>(R.id.finishButton)
        gridLayout     = findViewById<GridLayout>(R.id.cardGridLayout)
        endTimeText    = findViewById<TextView>(R.id.endTimeText)
        kartNextButton = findViewById<Button>(R.id.kartNextButton)
        totalWrongText = findViewById<TextView>(R.id.totalWrongText)

        fnishButton.visibility    = View.GONE
        endTimeText.visibility    = View.GONE
        gridLayout.visibility     = View.GONE
        kartNextButton.visibility = View.GONE
        totalWrongText.visibility = View.GONE
    }
}


