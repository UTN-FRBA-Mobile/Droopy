package com.example.droopy.ui.searches

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.droopy.R
import com.example.droopy.models.FilmSearch
import com.example.droopy.models.SearchInfo
import com.example.droopy.ui.maps.MapsActivity
import com.example.droopy.utils.Utils

@Composable
fun SearchInfoScreen(searchId: String) {
    val searchInfoViewModel = remember { SearchInfoViewModel() }
    val token = LocalContext.current.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("token", "")
    searchInfoViewModel.getSearchInfoById(searchId, token!!)

    val searchInfoState by searchInfoViewModel.searchInfoState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        searchInfoViewModel.searchInfoState.value?.let { searchInfo ->
            // Compose code to display the movie data in the Box
            SearchInfoCard(Modifier.align(Alignment.Center), searchInfo)
        } ?: run {
            // When search info is null
            Text(text = "error loading search info")
        }
    }
}

@Composable
private fun SearchInfoCard(modifier: Modifier, searchInfo: SearchInfo) {
    val status = Utils.translateSearchInfoStatus(searchInfo.searchStatus)
    val mContext = LocalContext.current
    Card(modifier.padding(4.dp), elevation = 8.dp) {
        Column(Modifier.padding(16.dp)) {
            AsyncImage(
                model = searchInfo.imageUrl,
                contentDescription = "Header"
            )
            Text(
                text = searchInfo.title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Divider(thickness = 1.dp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = searchInfo.description)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Estado: $status")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Se ofrece recompensa? ${
                    Utils.translateBool(
                        searchInfo.isPaid,
                        firstUpper = true
                    )
                }"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = Utils.toReadableString(searchInfo.createdAt))
            Spacer(modifier = Modifier.height(16.dp))
            // TODO: add some use about last updated at X minutes
            // TODO: add some info about requester company
            Button(
                onClick = {
                    mContext.startActivity(
                        Intent(
                            mContext,
                            MapsActivity::class.java
                        )
                    )
                },
                modifier = Modifier
                    .padding(4.dp)
                    .align(CenterHorizontally),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) { Text(text = "Volver") }
            Button(
                onClick = {},
                modifier = Modifier
                    .padding(4.dp)
                    .align(CenterHorizontally),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) { Text(text = "Transmitir") }
        }
    }
}