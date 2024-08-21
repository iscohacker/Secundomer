package uz.iskandarbek.secundomer

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var stopButton: Button
    private lateinit var flagButton: Button
    private lateinit var flagListView: ListView

    private var isRunning = false
    private var isPaused = false
    private var startTime = 0L
    private var timeBuffer = 0L
    private var updateTime = 0L
    private val handler = Handler()
    private val flagList = mutableListOf<String>()
    private lateinit var adapter: FlagAdapter

    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                updateTime = SystemClock.uptimeMillis() - startTime
                val updatedTime = timeBuffer + updateTime
                val seconds = (updatedTime / 1000).toInt()
                val minutes = (seconds / 60).toInt()
                val hours = (minutes / 60).toInt()
                val milliseconds = (updatedTime % 1000).toInt()

                timerTextView.text =
                    String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds % 60, milliseconds)
                handler.postDelayed(this, 10) // Refresh every 10ms for smoother update
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timer)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        stopButton = findViewById(R.id.stopButton)
        flagButton = findViewById(R.id.flagButton)
        flagListView = findViewById(R.id.flagListView)

        adapter = FlagAdapter(this, flagList)
        flagListView.adapter = adapter

        startButton.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                isPaused = false
                startTime = SystemClock.uptimeMillis()
                handler.post(runnable)
            }
        }

        pauseButton.setOnClickListener {
            if (isRunning && !isPaused) {
                isPaused = true
                timeBuffer += updateTime
                handler.removeCallbacks(runnable)
            } else if (isPaused) {
                isPaused = false
                startTime = SystemClock.uptimeMillis()
                handler.post(runnable)
            }
        }

        stopButton.setOnClickListener {
            if (isRunning) {
                isRunning = false
                isPaused = false
                timeBuffer = 0L
                updateTime = 0L
                timerTextView.text = "00:00:00:000"
                handler.removeCallbacks(runnable)
                flagList.add(timerTextView.text.toString()) // Add final time to the list
                adapter.notifyDataSetChanged()
                flagList.clear() // Clear flags after stop
                adapter.notifyDataSetChanged()
            }
        }

        flagButton.setOnClickListener {
            if (isRunning) {
                flagList.add(timerTextView.text.toString())
                adapter.notifyDataSetChanged()
            }
        }
    }
}
