package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
// Reference for import size:
// https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.ui.Modifier).size(androidx.compose.ui.unit.Dp)
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.FlowRow
// Reference: gemini.google.com
// Questions for Gemini: 
// What is the import to use a TextField in JetBrains Compose Multiplatform?
// Why is my build failing?
// --- Imports (Material 3 version) ---

// General Compose UI
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Material Design 3 (for TextField, Text, and potentially other M3 components like MaterialTheme)
// If you are wrapping your UI, you would typically use MaterialTheme from M3
// import androidx.compose.material3.MaterialTheme

// Compose State Management
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Foundation layout (for padding)
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollbarAdapter

import kotlindesktopapp.composeapp.generated.resources.Res
import kotlindesktopapp.composeapp.generated.resources.compose_multiplatform
import java.util.UUID

// Gemini: How do I append a TaskCard to the next row when clicking a button? 
// I want each TaskCard to be in a different row. When I click a button in a TaskCard, 
// it appends a new TaskCard in the next row underneath it.

// Reference for data class: https://kotlinlang.org/docs/data-classes.html
// Reference for UUID.randomUUID(): https://www.uuidgenerator.net/dev-corner/kotlin
// UUID.randomUUID().toString() is used to generate random Ids.

// Day could a single or multiple days: Examples (M-F, Mon-Fri, Tuesday, Sat-Sun, etc)
data class Day(
    val id: String = UUID.randomUUID().toString(), // unique id for each task
    var text: String
)

// This is used to create a list of tasks for the TextFields.
// A task could be a habit that the user is trying to build.
data class Task(
    val id: String = UUID.randomUUID().toString(), // unique id for each task
    var text: String
)

// A trigger is a signal that a user uses to start a task. So, if the user has "exercise" as a task.
// A trigger could be "dinner" which means after dinner, the user will start exercising.
data class Trigger(
    val id: String = UUID.randomUUID().toString(), // unique id for each task
    var text: String
)

// A distraction is any obstacle that the user may foresay that interfere with a building the specific habit.
// For example, a distraction could be "YouTube". So, the user may enter "YouTube" to commit to not do the distraction
// until after the task has been completed.
data class Distraction(
    val id: String = UUID.randomUUID().toString(), // unique id for each task
    var text: String
)

// This holds all the data for a specific task.
data class TaskCardData(
    val day: Day,
    val task: Task,
    val trigger: Trigger,
    val distraction: Distraction
) {
    val id: String = "${day.id}-${task.id}-${trigger.id}-${distraction.id}"
}

// References on how to build a card: 
// Components - https://developer.android.com/develop/ui/compose/components/card
// Size - https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#(androidx.compose.ui.Modifier).size(androidx.compose.ui.unit.Dp)
// Color - https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#size(androidx.compose.ui.unit.Dp)
// Button - https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#size(androidx.compose.ui.unit.Dp)
// Box - https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/package-summary#size(androidx.compose.ui.unit.Dp)
// Gemini: How do I do a hex color? - https://gemini.google.com/app
// Color Picker - https://www.w3schools.com/colors/colors_picker.asp
@Composable
fun TaskCard(
    day: Day,
    task: Task,
    trigger: Trigger,
    distraction: Distraction,
    onTextChangeDay: (String) -> Unit,
    onTextChangeTask: (String) -> Unit,
    onTextChangeTrigger: (String) -> Unit,
    onTextChangeDistraction: (String) -> Unit,
    addTaskCardBelow: (Day, Task, Trigger, Distraction) -> Unit,
    deleteTaskCard: (Day, Task, Trigger, Distraction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var taskCardBackgroundColor = Color(0xFFffcc99) // instead of #ffcc99, use 0xFFffcc99 - use prefix 0xFF
    var taskCardButtonColor = Color(0xFF333333)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = Modifier
            .size(width = 800.dp, height = 92.dp)
            .padding(10.dp, 2.dp)      
            // .background(Color.Green)
    ) {
        Box(
            Modifier.fillMaxWidth() // force a fixed size on the content
            .weight(1f)
            .background(taskCardBackgroundColor)
        ) {
            // Gemini: How do I position the TextField to the right of the button?
            // Answer: Use Row.
            // References to position using Row:
            // https://developer.android.com/develop/ui/compose/layouts/basics
            // https://www.jetpackcompose.net/compose-layout-row-and-column
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { addTaskCardBelow(day, task, trigger, distraction) },
                    // Modifier.align(Alignment.CenterStart).background(taskCardButtonColor),
                    // Gemini: How do I remove button color? - https://gemini.google.com/app
                    // Answer: Make it transparent.
                    colors = ButtonDefaults.buttonColors(
                        containerColor = taskCardButtonColor
                    )
                ) {
                    Text("+")
                }

                // References for TextField:
                // https://developer.android.com/develop/ui/compose/text/user-input?textfield=state-based
                // https://github.com/android/snippets/blob/7f4c676bb2c3b93491f25e01247115790ae1ea2f/compose/snippets/src/main/java/com/example/compose/snippets/text/StateBasedText.kt#L61-L64

                // These are initial values for the TextFields.
                // var inputValueDays by remember { mutableStateOf("M-F") }
                // var inputValueTask by remember { mutableStateOf("My task") }
                // var inputValueTrigger by remember { mutableStateOf("My Trigger") }
                // var inputValueDistraction by remember { mutableStateOf("My distraction") }

                // Gemini: How do I set the width of a TextField? - gemini.google.com/app
                // This answer didn't quite work right: modifier = Modifier.fillMaxWidth(0.2f) where 0.2 means 20% of the width of the parent
                // This answer worked: modifier = Modifier.weight(1f); This takes up 1 unit of space.

                TextField(
                    value = day.text, // inputValueDays,
                    onValueChange = onTextChangeDay, // { newText -> inputValueDays = newText }, // Update the state when text changes
                    label = { Text("Day(s)") },
                    modifier = Modifier.weight(1f).padding(8.dp)
                )

                TextField(
                    value = task.text, // This gets the text from the Task object.
                    onValueChange = onTextChangeTask, // { newText -> inputValueTask = newText }, // Update the state when text changes
                    label = { Text("Task") },
                    modifier = Modifier.weight(1f).padding(8.dp)
                )

                TextField(
                    value = trigger.text, // inputValueTrigger,
                    onValueChange = onTextChangeTrigger, // { newText -> inputValueTrigger = newText }, // Update the state when text changes
                    label = { Text("Trigger") },
                    modifier = Modifier.weight(1f).padding(8.dp)
                )

                TextField(
                    value = distraction.text, // inputValueDistraction,
                    onValueChange = onTextChangeDistraction, // { newText -> inputValueDistraction = newText }, // Update the state when text changes
                    label = { Text("Distraction") },
                    modifier = Modifier.weight(1f).padding(8.dp)
                )                

                Button(
                    onClick = { deleteTaskCard(day, task, trigger, distraction) },
                    // Modifier.align(Alignment.CenterEnd).background(taskCardButtonColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = taskCardButtonColor
                    )
                ) {
                    Text("-")
                }
            }
        }
    }
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Button(onClick = { showContent = !showContent }) {
            //     Text("Click me!")
            // }
            // AnimatedVisibility(showContent) {
            //     val greeting = remember { Greeting().greet() }
            //     Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            //         Image(painterResource(Res.drawable.compose_multiplatform), null)
            //         Text("Compose: $greeting")
            //     }
            // }

            // val days = remember { mutableStateListOf<Day>() }
            // This is for creating a list of tasks.
            // val tasks = remember { mutableStateListOf<Task>() }
            // val triggers = remember { mutableStateListOf<Trigger>() }
            // val distractions = remember { mutableStateListOf<Distraction>() }
            val taskCardData = remember { mutableStateListOf<TaskCardData>() }

            // This creates the intial TaskCard if does not exist.
            // It is used instead of doing this:
            // TaskCard()
            // LaunchedEffect(Unit) has it run only once at the start of the program.
            LaunchedEffect(Unit) {
                if (taskCardData.isEmpty()) {
                    val firstDay = Day(text = "M-F")
                    val firstTask = Task(text = "Task 1")
                    val firstTrigger = Trigger(text = "Trigger 1")
                    val firstDistraction = Distraction(text = "Distraction 1")

                    val firstTaskCard = TaskCardData(
                        day = firstDay,
                        task = firstTask,
                        trigger = firstTrigger,
                        distraction = firstDistraction
                    )

                    taskCardData.add(firstTaskCard)
                }
            }

            // Used for vertical scrollbar:
            val lazyListState = rememberLazyListState();

            // Box is needed for vertical scrollbar.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                // LazyColumn is used to show each TaskCard.
                // This interates through each task in the list called tasks.
                // LazyColumn is able to produce scrolling.
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { 
                    itemsIndexed(
                        items = taskCardData,
                        // Instead of using index in "key = { index, task -> task.id }",
                        // an underscore "_" is used because task.id is only needed for
                        // the key.
                        key = { _, item -> item.id } // This is a unique key for each taskCardData.
                    // index represents the position in the tasks list
                    // task represents the task object
                    ) { index, item ->
                        TaskCard(
                            day = item.day,
                            task = item.task,
                            trigger = item.trigger,
                            distraction = item.distraction,
                            onTextChangeDay = { newText ->
                                val updateDay = item.day.copy(text = newText) 
                                // days[index] = days[index].copy(text = newText)
                                val updateTaskCardData = item.copy(day = updateDay)
                                taskCardData[index] = updateTaskCardData
                            },
                            onTextChangeTask = { newText ->
                                val updateTask = item.task.copy(text = newText) 
                                val updateTaskCardData = item.copy(task = updateTask)
                                taskCardData[index] = updateTaskCardData
                            },
                            onTextChangeTrigger = { newText ->
                                val updateTrigger = item.trigger.copy(text = newText) 
                                val updateTaskCardData = item.copy(trigger = updateTrigger)
                                taskCardData[index] = updateTaskCardData
                            },
                            onTextChangeDistraction = { newText ->
                                val updateDistraction = item.distraction.copy(text = newText) 
                                val updateTaskCardData = item.copy(distraction = updateDistraction)
                                taskCardData[index] = updateTaskCardData
                            },
                            addTaskCardBelow = { dayOnClickedTaskCard, taskOnClickedTaskCard, triggerOnClickedTaskCard, distractionOnClickedTaskCard ->
                                val clickedTaskCard = TaskCardData(dayOnClickedTaskCard, taskOnClickedTaskCard, triggerOnClickedTaskCard, distractionOnClickedTaskCard)
                                val indexOfClickedTaskCard = taskCardData.indexOfFirst { item.id == clickedTaskCard.id }
                                val dayToBeAdded = Day(text = "M-F")
                                val taskToBeAdded = Task(text = "Task")
                                val triggerToBeAdded = Trigger(text = "Trigger")
                                val distractionToBeAdded = Distraction(text = "Distraction")

                                // If indexOfClickedTask is -1, then that means the index could not be found.
                                if (indexOfClickedTaskCard != -1) {
                                    taskCardData.add(indexOfClickedTaskCard + 1, TaskCardData(dayToBeAdded, taskToBeAdded, triggerToBeAdded, distractionToBeAdded))
                                }
                            },
                            deleteTaskCard = { dayOnClickedTaskCard, taskOnClickedTaskCard, triggerOnClickedTaskCard, distractionOnClickedTaskCard ->
                                val clickedTaskCard = TaskCardData(dayOnClickedTaskCard, taskOnClickedTaskCard, triggerOnClickedTaskCard, distractionOnClickedTaskCard)
                                val indexOfClickedTaskCard = taskCardData.indexOfFirst { item.id == clickedTaskCard.id }

                                // If indexOfClickedTask is -1, then that means the index could not be found.
                                if (indexOfClickedTaskCard != -1) {
                                    taskCardData.remove(clickedTaskCard)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(lazyListState)
                )
            }
        }
    }
}