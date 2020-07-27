package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest{

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {

        // Create an active tasks (the false makes this active)
        val tasks = listOf<Task>(
                Task("title", "desc", isCompleted = true),
                Task("title1", "desc1", isCompleted = true),
                Task("title3", "desc3", isCompleted = false),
                Task("title4", "desc4", isCompleted = false),
                Task("title5", "desc5", isCompleted = false)
        )
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.completedTasksPercent, `is`(40f))
        assertThat(result.activeTasksPercent,  `is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_ifNull_returnZeroes() {

        // Create an active tasks (the false makes this active)
        val tasks = null
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.completedTasksPercent,  `is`(0f))
        assertThat(result.activeTasksPercent, `is` (0f))

    }

    @Test
    fun getActiveAndCompletedStats_ifEmpty_returnZeroes() {

        // Create an active tasks (the false makes this active)
        val tasks = emptyList<Task>()
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.completedTasksPercent, `is` (0f))
        assertThat(result.activeTasksPercent, `is`(0f));













    }
}