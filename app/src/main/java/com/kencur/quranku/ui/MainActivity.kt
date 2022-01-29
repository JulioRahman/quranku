package com.kencur.quranku.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kencur.quranku.R
import com.kencur.quranku.model.Surah
import com.kencur.quranku.ui.theme.QurankuTheme
import com.kencur.quranku.utils.advanceShadow
import com.kencur.quranku.utils.elevation
import com.kencur.quranku.utils.getListSurah

class MainActivity : ComponentActivity() {

    private val listSurah: MutableLiveData<List<Surah>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            val surfaceColor = MaterialTheme.colors.surface

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = surfaceColor,
                    darkIcons = useDarkIcons
                )
            }

            GetScaffold()
        }

        getListSurah(this)?.let {
            listSurah.value = it
        }
    }

    @Composable
    fun GetScaffold() {
        QurankuTheme {
            val lazyColumnState = rememberLazyListState()

            Scaffold(
                topBar = { TopAppBarContent(lazyColumnState) },
                content = { MainContent(lazyColumnState) }
            )
        }
    }

    @Composable
    fun TopAppBarContent(lazyColumnState: LazyListState) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.surface,
            elevation = lazyColumnState.elevation
        )
    }

    @Composable
    fun MainContent(lazyColumnState: LazyListState) {
        val listSurah: List<Surah>? by listSurah.observeAsState()

        LazyColumn(
            state = lazyColumnState,
            contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                CardHeader()
            }

            // List Surah
            listSurah?.let {
                items(it) { surah ->
                    SurahItem(surah)
                }
            }
        }
    }

    @Composable
    fun CardHeader() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentColor = Color.White,
            elevation = 0.dp
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                Color(0xFF5EC3BF)
                            )
                        )
                    )
            ) {
                val (icLastRead, txtLastRead, txtSurah, txtAyah, imgQuran) = createRefs()

                Image(
                    painterResource(id = R.drawable.img_quran),
                    contentDescription = "Quran",
                    modifier = Modifier
                        .constrainAs(imgQuran) {
                            top.linkTo(parent.top, 32.dp)
                            end.linkTo(parent.end, (-40).dp)
                            bottom.linkTo(parent.bottom, (-32).dp)
                        }
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_last_read),
                    contentDescription = "icon",
                    modifier = Modifier.constrainAs(icLastRead) {
                        start.linkTo(parent.start, 24.dp)
                        top.linkTo(txtLastRead.top)
                        bottom.linkTo(txtLastRead.bottom)
                    }
                )

                Text(
                    text = "Terakhir Dibaca",
                    modifier = Modifier.constrainAs(txtLastRead) {
                        top.linkTo(parent.top, 24.dp)
                        start.linkTo(icLastRead.end, 8.dp)
                    },
                    style = MaterialTheme.typography.subtitle2
                )

                Text(
                    text = "Ayo Baca\nAl Quran",
                    modifier = Modifier.constrainAs(txtSurah) {
                        top.linkTo(txtLastRead.bottom, 32.dp)
                        start.linkTo(parent.start, 24.dp)
                    },
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "",
                    modifier = Modifier.constrainAs(txtAyah) {
                        top.linkTo(txtSurah.bottom)
                        start.linkTo(parent.start, 24.dp)
                        bottom.linkTo(parent.bottom, 24.dp)
                    },
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }

    @Composable
    fun SurahItem(surah: Surah) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .advanceShadow(
                    color = Color(0xFF8A8A8E),
                    alpha = 0.25F,
                    cornersRadius = 16.dp,
                    shadowBlurRadius = 16.dp,
                    offsetY = 2.dp
                ),
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .border(1.dp, MaterialTheme.colors.primary, CircleShape)
                        .align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = surah.index.toString(),
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp)
                ) {
                    Text(
                        text = surah.latin,
                        style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.SemiBold)
                    )

                    Text(
                        text = "${surah.translation}, ${surah.ayahCount} ayat",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }

    @Preview(device = Devices.PIXEL)
    @Composable
    fun DefaultPreview() {
        GetScaffold()
    }

    @Preview
    @Composable
    fun ItemPreview() {
        SurahItem(
            surah = Surah(
                1,
                "الفاتحة",
                "Al-Fatihah",
                "Pembukaan",
                7
            )
        )
    }
}