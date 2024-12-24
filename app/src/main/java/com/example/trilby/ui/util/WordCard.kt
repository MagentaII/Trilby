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
import com.example.trilby.data.sources.network.AppShortDef
import com.example.trilby.data.sources.network.Meta
import com.example.trilby.data.sources.network.NetworkWords
import com.example.trilby.ui.navigation.Route

@Composable
fun WordCard(
    word: NetworkWords,
    onNavigateToDetail: (route: Route, name: String) -> Unit,
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
                Route.WordDetail(id = word.meta.id),
                word.meta.id
            )
        },
    ) {
        Text(
            text = word.meta.appShortDef.headword,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(start = 17.dp, top = 20.dp, bottom = 20.dp)
        )
    }
}


@Preview
@Composable
private fun WordCardView() {
    val defaultWord = NetworkWords(
        meta = Meta(
            id = "apple",
            appShortDef = AppShortDef(
                headword = "apple",
                label = "noun",
                definition = listOf("{bc} a round fruit with red, yellow, or green skin and firm white flesh")
            )
        )
    )
    WordCard(
        defaultWord,
        onNavigateToDetail = { route, name ->
            Log.i("TAG", "WordCardView, route, name: $route and $name")
        }
    )
}