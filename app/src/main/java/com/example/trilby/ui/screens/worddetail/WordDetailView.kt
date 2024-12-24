package com.example.trilby.ui.screens.worddetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trilby.data.sources.network.NetworkWords
import com.example.trilby.ui.navigation.Route

@Composable
fun WordDetailView(
    wordDetail: Route.WordDetail,
    words: List<NetworkWords> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 17.dp, vertical = 28.dp)
    ) {
        val word = words.find { it.id == wordDetail.id }
        Text(
            text = word?.headword ?: "headword error",
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Medium),
        )

        Spacer(Modifier.height(28.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp)
        ) {
            Text(
                text = word?.label ?: "label error",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
            )
            IconButton(
                onClick = { }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        Text(
            text = word?.definition ?: "definition error",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 32.sp
            ),
        )
        Spacer(Modifier.height(24.dp))
        Divider()
    }
}

@Preview(showBackground = true)
@Composable
private fun WordDetailViewPreview() {
    val defaultWord = NetworkWords(
        id = "apple",
        headword = "apple",
        label = "noun",
        definition = "{bc} a round fruit with red, yellow, or green skin and firm white flesh"
    )
    val wordDetail = Route.WordDetail(defaultWord.id)
    WordDetailView(wordDetail)
}