package com.example.learntrack.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.learntrack.domain.model.Subject
import com.example.learntrack.utils.StudyConstants.TAGS.DASHBOARD_TAG

@Composable
fun AddSubjectDialog(
    isOpen: Boolean,
    title: String,
    subjectGoalHour: String,
    subjectName: String,
    selectedColorList: List<Color>,
    onColorChange: (List<Color>) -> Unit,
    onSubjectGoalHourChange: (String) -> Unit,
    onSubjectNameChange: (String) -> Unit,
    onDismissRequestClick: () -> Unit,
    onConfirmButtonClick: () -> Unit,
) {

    var subjectNameError by rememberSaveable { mutableStateOf<String?>(value = null) }
    var goalHoursError by rememberSaveable { mutableStateOf<String?>(value = null) }

    subjectNameError = when {
        subjectName.isBlank() -> "Please enter subject name."
        subjectName.length < 2 -> "Subject name is too short."
        subjectName.length > 20 -> "Subject name is too long."
        else -> null
    }

    goalHoursError = when {
        subjectGoalHour.isBlank() -> "Please enter goal study hours."
        subjectGoalHour.toFloatOrNull() == null -> "Invalid number."
        subjectGoalHour.toFloat() < 1f -> "Please set at least 1 hour."
        subjectGoalHour.toFloat() > 1000f -> "Please set a maximum of 1000 hours."
        else -> null
    }

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequestClick,
            title = { Text(text = title) },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Subject.subjectCardColors.forEach { colors ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.dp,
                                        color = if (colors == selectedColorList) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .background(brush = Brush.verticalGradient(colors))
                                    .clickable {
                                        Log.d(DASHBOARD_TAG, "AddSubjectDialog: colors = $colors")
                                        onColorChange(colors)
                                    }
                            ) {

                            }
                        }
                    }

                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = onSubjectNameChange,
                        label = { Text(text = "Subject Name") },
                        singleLine = true,
                        isError = subjectNameError != null && subjectName.isNotEmpty(),
                        supportingText = { Text(text = subjectName.orEmpty()) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = subjectGoalHour,
                        onValueChange = onSubjectGoalHourChange,
                        label = { Text(text = "Goal study hours") },
                        singleLine = true,
                        isError = goalHoursError != null && subjectGoalHour.isNotEmpty(),
                        supportingText = { Text(text = goalHoursError.orEmpty()) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequestClick) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonClick,
                    enabled = goalHoursError == null && subjectNameError == null
                ) {
                    Text(text = "Save")
                }
            },
        )
    }
}