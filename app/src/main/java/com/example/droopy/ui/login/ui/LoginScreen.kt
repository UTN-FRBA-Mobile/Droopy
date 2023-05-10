package com.example.droopy.ui.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.droopy.R

@Composable
fun LoginScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center))
    }
}

@Composable
fun Login(modifier: Modifier) {
    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField()
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField()
    }
}

@Composable
fun PasswordField() {
    TextField(
        value = "", onValueChange = {},
        placeholder = { Text(text = "Contrasenia") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF4548BB),
            backgroundColor = Color(0xFFEDEBF1),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmailField() {
    TextField(
        value = "", onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color(0xFF4548BB),
            backgroundColor = Color(0xFFEDEBF1),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.droopy),
        contentDescription = "Header",
        modifier = Modifier
    )
}