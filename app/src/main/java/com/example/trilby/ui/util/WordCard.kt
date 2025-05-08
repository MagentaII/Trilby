package com.example.trilby.ui.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import timber.log.Timber

@Composable
fun WordCard(
    word: ShowWord,
//    onNavigateToDetail: (route: Route) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(containerColor = Color.White),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.LightGray),
        onClick = {onItemClick(word.uid)}
//        onClick = {
//            onNavigateToDetail(
//                Route.WordDetail(
//                    uid = word.uid,
//                ),
//            )
//        },
    ) {
        Text(
            text = word.uid,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(start = 17.dp, top = 20.dp, bottom = 20.dp)
        )
    }
}


@Preview
@Composable
private fun WordCardView() {
    val defaultWord = ShowWord(
        uid = "book",
        words = emptyList()
    )

    WordCard(
        defaultWord,
        onItemClick = { id ->
            Timber.d("WordCardView, id: $id")
        }
//        onNavigateToDetail = { route ->
//            Log.i("TAG", "WordCardView, route, name: $route")
//        }
    )
}