package com.example.secondzaets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.secondzaets.databinding.SecondActivityBinding

class SecondActivity: AppCompatActivity() {
    private lateinit var binding: SecondActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SecondActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
    }

    private fun setupData() {
        binding.tvInt.text = intent.getStringExtra(RECEIVED_STRING)
        binding.tvString.text = intent.getIntExtra(RECEIVED_INT, 0).toString()
    }

    companion object {
        private const val RECEIVED_STRING = "RECEIVED_STRING"
        private const val RECEIVED_INT = "RECEIVED_INT"

        fun start(context: Context, string: String, int: Int) {
            val intent = Intent(context, SecondActivity::class.java)
            intent.putExtra(RECEIVED_INT, int)
            intent.putExtra(RECEIVED_STRING, string)
            context.startActivity(intent)
        }
    }
}