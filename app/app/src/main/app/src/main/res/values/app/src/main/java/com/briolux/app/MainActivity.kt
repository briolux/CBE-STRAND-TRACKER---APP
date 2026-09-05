package com.briolux.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class StudentRecord(
    val name: String,
    val admissionNumber: String,
    val subject: String,
    val topic: String,
    val score: Int
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BrioluxApp()
        }
    }
}

@Composable
fun BrioluxApp() {

    var selectedTab by remember { mutableStateOf(0) }

    var records by remember {
        mutableStateOf(listOf<StudentRecord>())
    }

    val subjects = listOf(
        "Mathematics",
        "English",
        "Kiswahili",
        "Physics",
        "Chemistry",
        "Biology",
        "Computer Science",
        "Agriculture",
        "Business Studies",
        "Geography",
        "History & Government",
        "CRE",
        "IRE",
        "French",
        "German",
        "Music",
        "Physical Education"
    )

    MaterialTheme {

        Scaffold(

            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "BRIOLUX",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                )
            },

            bottomBar = {

                NavigationBar {

                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {},
                        label = { Text("Dashboard") }
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {},
                        label = { Text("Students") }
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {},
                        label = { Text("Marks") }
                    )
                }
            }

        ) { padding ->

            when (selectedTab) {

                0 -> Dashboard(
                    padding = padding,
                    records = records
                )

                1 -> StudentEntry(
                    padding = padding,
                    subjects = subjects
                ) { record ->

                    records = records + record
                }

                2 -> MarksView(
                    padding = padding,
                    records = records
                )
            }
        }
    }
}

@Composable
fun Dashboard(
    padding: PaddingValues,
    records: List<StudentRecord>
) {

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(20.dp)
    ) {

        Text(
            "CBE Performance Tracker",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    "Performance Records",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "${records.size}",
                    style = MaterialTheme.typography.displaySmall
                )

                Text("records entered")
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "BRIOLUX helps teachers track learner performance by subject and topic."
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "Duplicate student names and admission numbers are allowed."
        )
    }
}

@Composable
fun StudentEntry(
    padding: PaddingValues,
    subjects: List<String>,
    onAdd: (StudentRecord) -> Unit
) {

    var name by remember { mutableStateOf("") }

    var admission by remember {
        mutableStateOf("")
    }

    var subject by remember {
        mutableStateOf(subjects.first())
    }

    var topic by remember {
        mutableStateOf("")
    }

    var score by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {

        Text(
            "Add Performance Record",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = admission,
            onValueChange = { admission = it },
            label = { Text("Admission number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Text("Select Subject")

        LazyColumn(
            modifier = Modifier.height(130.dp)
        ) {

            items(subjects) { currentSubject ->

                TextButton(
                    onClick = {
                        subject = currentSubject
                    }
                ) {

                    Text(
                        if (currentSubject == subject)
                            "✓ $currentSubject"
                        else
                            currentSubject
                    )
                }
            }
        }

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Topic") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = score,
            onValueChange = {
                score = it.filter { character ->
                    character.isDigit()
                }
            },
            label = { Text("Score (%)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {

                val mark = score.toIntOrNull()

                if (
                    name.isNotBlank() &&
                    admission.isNotBlank() &&
                    topic.isNotBlank() &&
                    mark != null &&
                    mark in 0..100
                ) {

                    onAdd(
                        StudentRecord(
                            name = name,
                            admissionNumber = admission,
                            subject = subject,
                            topic = topic,
                            score = mark
                        )
                    )

                    message = "Record saved successfully."

                    name = ""
                    admission = ""
                    topic = ""
                    score = ""

                } else {

                    message =
                        "Please complete all fields and enter a score from 0–100."
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Save Record")
        }

        if (message.isNotBlank()) {

            Spacer(Modifier.height(8.dp))

            Text(message)
        }
    }
}

@Composable
fun MarksView(
    padding: PaddingValues,
    records: List<StudentRecord>
) {

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {

        Text(
            "Performance Records",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(10.dp))

        if (records.isEmpty()) {

            Text("No records have been entered yet.")

        } else {

            LazyColumn {

                items(records) { record ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            Text(
                                record.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "Admission: ${record.admissionNumber}"
                            )

                            Text(
                                "${record.subject} • ${record.topic}"
                            )

                            Text(
                                "Score: ${record.score}%"
                            )
                        }
                    }
                }
            }
        }
    }
}
