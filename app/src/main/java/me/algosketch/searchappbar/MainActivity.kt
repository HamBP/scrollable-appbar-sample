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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import me.algosketch.searchappbar.ui.SearchViewModel
import me.algosketch.searchappbar.ui.theme.SearchAppBarTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchAppBarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SampleScreen(viewModel = SearchViewModel())
                }
            }
        }
    }
}

@Composable
fun SampleScreen(viewModel: SearchViewModel) {
    val keyword by viewModel.keyword.collectAsState()
    val titles by viewModel.titles.collectAsState()

    val scrollableHeight = 80.dp
    val appBarHeight = 160.dp
    val scrollableHeightPx = with(LocalDensity.current) { scrollableHeight.roundToPx().toFloat() }
    var appbarOffsetHeightPx by remember { mutableFloatStateOf(0f) }

    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.refresh.collect {
            lazyColumnState.animateScrollToItem(0, 0)
            appbarOffsetHeightPx = 0f
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                appbarOffsetHeightPx += available.y

                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                appbarOffsetHeightPx -= available.y

                return Offset.Zero
            }
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(nestedScrollConnection),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = lazyColumnState,
                contentPadding = PaddingValues(top = appBarHeight),
            ) {
                items(titles.size, key = { it }) {
                    Text(
                        text = titles[it],
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            }
            SearchAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(appBarHeight)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = appbarOffsetHeightPx
                                .coerceIn(-scrollableHeightPx, 0f)
                                .roundToInt()
                        )
                    },
                scrollableHeight = scrollableHeight,
                keyword = keyword,
                onKeywordChanged = viewModel::updateKeyword,
            )
        }
    }
}

@Composable
fun SearchAppBar(
    scrollableHeight: Dp,
    keyword: String,
    onKeywordChanged: (keyword: String) -> Unit,
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
            value = keyword,
            onValueChange = onKeywordChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
}
