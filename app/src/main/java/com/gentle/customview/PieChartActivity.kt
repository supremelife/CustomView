package com.gentle.customview

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.gentle.customview.databinding.ActivityPieChartBinding

/**
 *
 *
 * Author: LJL
 * Date: 2025/3/15
 */
class PieChartActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPieChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chartView.apply {
//            max = 1f

        }
    }

}