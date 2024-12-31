package com.example.trilby.ui.screens.worddetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trilby.ui.navigation.Route

@Composable
fun WordDetailView(
    wordDetail: Route.WordDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 28.dp)
    ) {
        // Header Section
        Log.i("TAG", "WordDetailView, headwords: ${wordDetail.headword}")
        HeaderSection(word = wordDetail.headword[0])

        Spacer(Modifier.height(14.dp))
        Divider(
            color = Color(0xFFC3CFEA), thickness = 3.dp, modifier = Modifier
                .width(250.dp)
                .padding(start = 8.dp)
        )


        wordDetail.label.forEachIndexed { index, label ->
            Spacer(Modifier.height(14.dp))
            // Word Details Section
            WordDetailsSection(
                label = label,
                definitions = wordDetail.definition[index].split("\n")
            )
        }


//        Spacer(Modifier.height(14.dp))
//        WordDetailsSection(
//            label = wordDetail.label[1], definitions = listOf(
//                wordDetail.definition[1][0],
//                wordDetail.definition[1][1],
//            )
//        )
//
//        Spacer(Modifier.height(14.dp))
//        WordDetailsSection(
//            label = wordDetail.label[2], definitions = listOf(
//                wordDetail.definition[2][0],
//                wordDetail.definition[2][1],
//            )
//        )
    }
}

@Composable
fun HeaderSection(word: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 24.dp, start = 8.dp)
    ) {
        Text(
            text = word,
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Medium),
        )
        IconButton(onClick = { /* TODO: Add click action */ }) {
            Icon(
                Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun WordDetailsSection(label: String, definitions: List<String>) {
    Text(
        text = label,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF0047E8)
        ),
        modifier = Modifier.padding(start = 8.dp)
    )
    Spacer(Modifier.height(14.dp))

    Box(
        modifier = Modifier.border(
            width = 3.dp,
            color = Color(0xFFC3CFEA),
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Spacer(Modifier.height(24.dp))
            definitions.forEachIndexed { index, definition ->
                DefinitionRow(index + 1, definition)
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DefinitionRow(index: Int, definition: String) {
    val splitDefinition = definition.split("\n")

    Row {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(22.dp)
                .background(color = Color(0xFFC3CFEA), shape = CircleShape)
        ) {
            Text(
                text = index.toString(),
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
            )
        }
        Spacer(Modifier.width(25.dp))
        Column {
            Text(
                text = splitDefinition[0],
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp
                )
            )
            if (splitDefinition.size > 1) {
                Text(
                    text = splitDefinition[1],
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF545454),
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=392.7dp,height=1500dp,dpi=440")
@Composable
private fun WordDetailViewPreview() {
    val defaultWord = Route.WordDetail(
        uid = "book",
        wordId = listOf("book:1", "book:2", "book:3"),
        headword = listOf("book", "book", "book"),
        label = listOf("noun", "adjective", "verb"),
        definition = listOf(
            "a set of written sheets of skin or paper or tablets of wood or ivory\n" +
                    "a set of written, printed, or blank sheets bound together between a front and back cover\n" +
                    "a long written or printed literary composition",
            "derived from books and not from practical experience\n" +
                    "shown by ledgers",
            "to register (something, such as a name) for some future activity or condition (as to engage transportation or reserve lodgings)\n" +
                    "to schedule engagements for\n" +
                    "to set aside time for",
        )
    )
//    val defaultShowWord = ShowWord(
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
    WordDetailView(defaultWord)
}