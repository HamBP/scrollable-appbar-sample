package me.algosketch.searchappbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.algosketch.searchappbar.ui.theme.SearchAppBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchAppBarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SampleScreen()
                }
            }
        }
    }
}

@Composable
fun SampleScreen() {
    val scrollableHeight = 80.dp
    val appBarHeight = 160.dp

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(top = appBarHeight)
            ) {
                items(100) {
                    Text(
                        text = "Hello Compose! $it",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            }
            SearchAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(appBarHeight),
                scrollableHeight = scrollableHeight,
            )
        }
    }
}

@Composable
fun SearchAppBar(
    scrollableHeight: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .height(scrollableHeight)
                .fillMaxWidth()
                .background(Color.Gray),
            text = "스크롤 가능한 부분",
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
        )
    }
}
