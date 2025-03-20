package org.savics.littlelemon

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Serializable
data class MenuNetwork(
    @SerialName("menu") val menu: List<MenuItemNetwork>
)

@Serializable
data class MenuItemNetwork(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: String,
    @SerialName("image") val image: String,
    @SerialName("category") val category: String
)

object NetworkUtils {
    val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
                encodeDefaults = true
            })
        }
    }

    suspend fun fetchMenuData(): MenuNetwork {
        return try {
            // Fetch the raw content as text
            val rawResponse: String =
                httpClient.get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json") {
                    accept(ContentType.Any) // Accept raw content
                }.body()

            // Deserialize the JSON string into MenuNetwork object
            Json.decodeFromString<MenuNetwork>(rawResponse)

        } catch (e: Exception) {
            // Handle the error gracefully
            e.printStackTrace()
            // Return an empty list to prevent crashing
            MenuNetwork(menu = emptyList())
        }
    }
}