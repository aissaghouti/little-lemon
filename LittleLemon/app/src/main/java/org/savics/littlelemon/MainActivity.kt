package org.savics.littlelemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.savics.littlelemon.ui.theme.LittleLemonTheme
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var database: AppDatabase
    private lateinit var menuItemsLiveData: LiveData<List<MenuItemRoom>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database
        database = AppDatabase.getDatabase(applicationContext)
        menuItemsLiveData = database.menuItemDao().getAllMenuItems()

        // Fetch menu items from network and store in database
        fetchMenuDataAndStoreInDb()

        enableEdgeToEdge()
        setContent {
            LittleLemonTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    Navigation(navController = navController, context = this, menuItemsLiveData = menuItemsLiveData)
                }
            }
        }
    }

    private fun fetchMenuDataAndStoreInDb() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val menuNetwork = NetworkUtils.fetchMenuData()

                // Map network models to Room entities
                val menuItemsRoom = menuNetwork.menu.map {
                    MenuItemRoom(
                        id = it.id,
                        title = it.title,
                        description = it.description,
                        price = it.price,
                        image = it.image,
                        category = it.category
                    )
                }

                // Convert numeric prices for better display
                val formattedMenuItems = menuItemsRoom.map { item ->
                    try {
                        val numericPrice = item.price.toDouble()
                        item.copy(price = String.format("%.2f", numericPrice))
                    } catch (e: Exception) {
                        item
                    }
                }

                // Save to database
                database.menuItemDao().insertAll(formattedMenuItems)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}