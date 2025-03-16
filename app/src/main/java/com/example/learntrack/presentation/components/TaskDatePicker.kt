package com.example.learntrack.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    state: DatePickerState,
    isOpen: Boolean,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onDismissRequestClicked: () -> Unit,
    onConfirmButtonClicked: () -> Unit,
) {

    val selectedDate = state.selectedDateMillis
    val todayMillis = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val isValidDate = selectedDate == null || selectedDate >= todayMillis

    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = onDismissRequestClicked,
            confirmButton = {
                TextButton(onClick = onConfirmButtonClicked, enabled = isValidDate) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequestClicked) {
                    Text(text = dismissButtonText)
                }
            },
            content = {
                DatePicker(state = state)
            }
        )

        if (!isValidDate) {
            Text(
                "Please select a future date",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}