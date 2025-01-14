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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.data.repositories.word_repository.Word
import com.example.trilby.data.repositories.word_repository.WordPrs
import com.example.trilby.data.repositories.word_repository.WordSound
import com.example.trilby.ui.navigation.Route

@Composable
fun WordDetailView(
    wordDetail: Route.WordDetail,
    words: List<ShowWord>,
    viewModel: WordDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val selectedWord = words.find { it.uid == wordDetail.uid }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 28.dp)
    ) {
        // Header Section
        Log.i("TAG", "WordDetailView, headwords: ${selectedWord}")
        HeaderSection(
            word = selectedWord?.uid ?: "Error"
        )

        Spacer(Modifier.height(14.dp))
        HorizontalDivider(
            modifier = Modifier
                .width(250.dp)
                .padding(start = 8.dp),
            thickness = 3.dp,
            color = Color(0xFFC3CFEA)
        )

        selectedWord?.words?.forEachIndexed { index, word ->
            Spacer(Modifier.height(14.dp))
            // Word Details Section
            WordDetailsSection(
                viewModel = viewModel,
                label = word.label,
                prs = word.wordPrs,
                shortDef = word.shortDef
            )
        }
    }
}

@Composable
fun HeaderSection(
    word: String
) {
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
    }
}

@Composable
fun WordDetailsSection(
    viewModel: WordDetailViewModel,
    label: String,
    prs: List<WordPrs>?,
    shortDef: List<String>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0047E8)
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
        prs?.forEach { pr ->
            if (pr.sound != null) {
                Spacer(Modifier.width(16.dp))
                PronunciationAndSound(
                    viewModel = viewModel,
                    wordPrs = pr
                )
            }
        }
    }
    Spacer(Modifier.height(14.dp))

    Box(
        modifier = Modifier
            .border(
                width = 3.dp,
                color = Color(0xFFC3CFEA),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Spacer(Modifier.height(24.dp))
            shortDef.forEachIndexed { index, definition ->
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

@Composable
fun PronunciationAndSound(
    viewModel: WordDetailViewModel,
    wordPrs: WordPrs,
) {
    OutlinedButton(
        onClick = {
            viewModel.playWordAudio(wordPrs)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = wordPrs.mw
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=392.7dp,height=1500dp,dpi=440")
@Composable
private fun WordDetailViewPreview() {
    val fakeWords = listOf(
        ShowWord(
            uid = "book",
            words = listOf(
                Word(
                    wordId = "book:1",
                    headword = "book",
                    wordPrs = listOf(
                        WordPrs(
                            mw = "ˈbu̇k",
                            sound = WordSound(
                                audio = "book0001",
                                ref = "c",
                                stat = "1",
                                subdirectory = "b"
                            )
                        )
                    ),
                    label = "noun",
                    shortDef = listOf(
                        "a set of written sheets of skin or paper or tablets of wood or ivory",
                        "a set of written, printed, or blank sheets bound together between a front and back cover",
                        "a long written or printed literary composition"
                    ),
                )
            )
        )
    )

    val wordDetail = Route.WordDetail(
        uid = "book"
    )

    WordDetailView(
        wordDetail = wordDetail,
        words = fakeWords,
    )
}