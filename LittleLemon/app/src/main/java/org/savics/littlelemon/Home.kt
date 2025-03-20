package org.savics.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

// Define colors to match the mockup
val littleLemonGreen = Color(0xFF495E57)
val littleLemonYellow = Color(0xFFF4CE14)
val lightGray = Color(0xFFF0F0F0)

@Composable
fun Home(navController: NavHostController, menuItemsLiveData: LiveData<List<MenuItemRoom>>? = null) {
    // Observe menu items from database
    val menuItems by menuItemsLiveData?.observeAsState(emptyList()) ?: remember { mutableStateOf(emptyList()) }

    // State for category filter
    var selectedCategory by remember { mutableStateOf("") }

    // State for search phrase
    var searchPhrase by remember { mutableStateOf("") }

    // Filter menu items by search phrase and category
    val filteredItems = menuItems.filter { menuItem ->
        (searchPhrase.isEmpty() || menuItem.title.contains(searchPhrase, ignoreCase = true)) &&
                (selectedCategory.isEmpty() || menuItem.category.equals(selectedCategory, ignoreCase = true))
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with logo and profile icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Spacer for left alignment
            Spacer(modifier = Modifier.weight(0.2f))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier
                    .weight(0.6f)
                    .height(56.dp)
            )

            // Profile icon
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .weight(0.2f)
                    .clickable {
                        navController.navigate(Profile.route)
                    }
            )
        }

        // Hero Section with search functionality
        HeroSection(searchPhrase = searchPhrase, onSearchPhraseChanged = { searchPhrase = it })

        // Category filters and menu items
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Text(
                    text = "ORDER FOR DELIVERY!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                CategoryFilters(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }

            // Divider
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                        .padding(horizontal = 16.dp)
                )
            }

            // Menu Items - filtered by both search phrase and category
            items(filteredItems) { menuItem ->
                MenuItem(menuItem = menuItem)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroSection(searchPhrase: String, onSearchPhraseChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(littleLemonGreen)
            .padding(16.dp)
    ) {
        Text(
            text = "Little Lemon",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = littleLemonYellow
        )

        Text(
            text = "Chicago",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.weight(0.6f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.hero),
                contentDescription = "Restaurant Food",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .weight(0.4f)
            )
        }

        // Search Bar - Updated to use the passed parameters
        OutlinedTextField(
            value = searchPhrase,
            onValueChange = { onSearchPhraseChanged(it) },
            placeholder = { Text("Enter search phrase", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                containerColor = Color.White
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(littleLemonGreen)
            .padding(16.dp)
    ) {
        Text(
            text = "Little Lemon",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = littleLemonYellow
        )

        Text(
            text = "Chicago",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.weight(0.6f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.hero),
                contentDescription = "Restaurant Food",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .weight(0.4f)
            )
        }

        // Search Bar
        var searchText by remember { mutableStateOf("") }

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Enter search phrase", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                containerColor = Color.White
            )
        )
    }
}

@Composable
fun CategoryFilters(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Starters", "Mains", "Desserts", "Drinks")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                isSelected = category.equals(selectedCategory, ignoreCase = true),
                onSelected = {
                    if (category.equals(selectedCategory, ignoreCase = true)) {
                        onCategorySelected("")  // Deselect if already selected
                    } else {
                        onCategorySelected(category)
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryChip(category: String, isSelected: Boolean, onSelected: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelected() },
        color = if (isSelected) littleLemonGreen else lightGray
    ) {
        Text(
            text = category,
            color = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItem(menuItem: MenuItemRoom) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = menuItem.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = menuItem.description,
            fontSize = 16.sp,
            color = Color.Gray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calculate price with $ prefix - assume price is a numeric string
            val displayPrice = try {
                val numericPrice = menuItem.price.toDouble()
                "$${String.format("%.2f", numericPrice)}"
            } catch (e: Exception) {
                "$${menuItem.price}"
            }

            Text(
                text = displayPrice,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            GlideImage(
                model = menuItem.image,
                contentDescription = menuItem.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        // Divider between menu items
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
                .padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home(rememberNavController())
}