package com.example.learntrack.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.learntrack.R
import com.example.learntrack.domain.model.Session

fun LazyListScope.studySessionList(
    sectionTitle: String,
    sessions: List<Session>,
    emptyListText: String,
    onDeleteSessionClick: (Session) -> Unit,
) {
    item {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(12.dp)
        )
    }

    if (sessions.isEmpty()) {

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(120.dp),
                    painter = painterResource(id = R.drawable.ic_edu_lamp),
                    contentDescription = emptyListText
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )
                Text(
                    text = emptyListText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    items(sessions) { session ->
        SessionCard(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            sessions = session,
            onDeleteSessionClick = { onDeleteSessionClick(session) }
        )
    }
}

@Composable
private fun SessionCard(
    modifier: Modifier,
    sessions: Session,
    onDeleteSessionClick: () -> Unit,
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = sessions.relatedToSubject,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "${sessions.duration} hr", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "${sessions.duration} hr", style = MaterialTheme.typography.bodySmall)

            IconButton(onClick = { onDeleteSessionClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")
            }
        }
    }
}