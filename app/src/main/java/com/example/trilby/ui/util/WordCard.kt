package com.example.trilby.ui.util

import android.util.Log
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
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.ui.navigation.Route

@Composable
fun WordCard(
    word: ShowWord,
    onNavigateToDetail: (route: Route, word: ShowWord) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(containerColor = Color.White),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.LightGray),
        onClick = {
            onNavigateToDetail(
                Route.WordDetail(
                    uid = word.uid,
                ),
                word
            )
        },
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
//    val defaultWord = ShowWord(
//        uid = "book",
//        wordId = listOf("book:1", "book:2", "book:3"),
//        headword = listOf("book", "book", "book"),
//        label = listOf("noun", "adjective", "verb"),
//        definition = listOf(
//            listOf(
//                "a set of written sheets of skin or paper or tablets of wood or ivory",
//                "a set of written, printed, or blank sheets bound together between a front and back cover",
//                "a long written or printed literary composition",
//            ),
//            listOf(
//                "derived from books and not from practical experience",
//                "shown by ledgers"
//            ),
//            listOf(
//                "to register (something, such as a name) for some future activity or condition (as to engage transportation or reserve lodgings)",
//                "to schedule engagements for",
//                "to set aside time for"
//            )
//        )
//    )

    WordCard(
        defaultWord,
        onNavigateToDetail = { route, name ->
            Log.i("TAG", "WordCardView, route, name: $route and $name")
        }
    )
}