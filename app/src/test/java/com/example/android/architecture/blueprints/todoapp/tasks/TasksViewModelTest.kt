package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


class TasksViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Old way, replaced with MainCoroutineRule

    /*@ExperimentalCoroutinesApi
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }*/


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var taskDetailViewModel: TaskDetailViewModel
    private lateinit var tasksRepository: FakeTestRepository
    private lateinit var tasksRepository2: FakeTestRepository
    @Before
    fun setupViewModel() {
        tasksRepository = FakeTestRepository()
        tasksRepository2 = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        val task4 = Task("Title4", "Description4", true)
        val task5 = Task("Title5", "Description5", true)
        val task6 = Task("Title6", "Description6", true)
        tasksRepository.addTasks(task1, task2, task3)

        tasksRepository2.addTasks(task4, task5, task6)
        tasksViewModel = TasksViewModel(tasksRepository)
        taskDetailViewModel = TaskDetailViewModel(tasksRepository2)
    }





    @Test
    fun addNewTask_setsNewTaskEvent() {
           // Long way example
        /* // Given a fresh ViewModel
            val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
            //Create observer - no need for it to do anything
            val observer = Observer<Event<Unit>> {}

               try {
                   //Observe LiveData forever
                   tasksViewModel.newTaskEvent.observeForever(observer)

                   // When adding a new task
                   tasksViewModel.addNewTask()

                   // Then the new task event is triggered
                   val value = tasksViewModel.newTaskEvent.value
                   assertThat(value?.getContentIfNotHandled(), not((nullValue())))
               } finally {
                   //Don't forget to remove the observer
                   tasksViewModel.newTaskEvent.removeObserver(observer)
               }
        */

        // Fast way
        // Given a fresh ViewModel
        //  val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(
                value.getContentIfNotHandled(), (not(nullValue()))
        )
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {

        // Given a fresh ViewModel
        //  val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value, `is`(true))
    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Create an active task and add it to the repository.
        val task = Task("Title", "Description")
        tasksRepository.addTasks(task)

        // Mark the task as complete task.
        tasksViewModel.completeTask(task, true)

        // Verify the task is completed.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}