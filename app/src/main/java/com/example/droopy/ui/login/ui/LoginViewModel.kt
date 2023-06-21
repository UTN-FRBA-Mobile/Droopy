package com.example.droopy.ui.login.ui

import android.content.Context
import android.content.Intent
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.droopy.models.LoginRequestBody
import com.example.droopy.ui.api.ApiService
import com.example.droopy.ui.maps.MapsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel : ViewModel() {
    private val baseUrl = "http://192.168.0.142:3001/api/"
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email)
    }

    suspend fun onLoginSelected(context: Context, token: String) {
        _isLoading.value = true
        delay(1000)
        _isLoading.value = false
        if (token != "") {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("token", token).apply()

            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    suspend fun login() {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            val requestBody = LoginRequestBody(
                email = email.value ?: "",
                password = password.value ?: ""
            )
            val response = apiService.login(requestBody)
            withContext(Dispatchers.Main) {
                if (response.token != null) {
                    _token.value = response.token
                    println("PRIMER TOKEN: $_token")
                } else {
                    _errorMessage.value = "No se recibió un token válido"
                }
            }
        } catch (e: HttpException) {
            if (e.code() == 401) {
                _errorMessage.value = "Credenciales Inválidas"
            } else {
                _errorMessage.value = "Error en la petición: ${e.code()}"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Error en la conexión"
        }
    }
}
