package com.example.learntrack.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    isOpen: Boolean,
    title: String,
    description: String,
    onDismissRequestClick: () -> Unit,
    onConfirmButtonClick: () -> Unit,
) {

    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequestClick,
            title = { Text(text = title) },
            text = {
                Text(text = description)
            },
            dismissButton = {
                TextButton(onClick = onDismissRequestClick) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmButtonClick
                ) {
                    Text(text = "Save")
                }
            },
        )
    }
}