package org.savics.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Onboarding() {
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo - Increased size
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Little Lemon Logo",
            modifier = Modifier
                .padding(top = 24.dp, bottom = 16.dp)
                .fillMaxWidth(0.6f)
                .height(80.dp)
        )

        // Header with Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF495E57))
                .padding(vertical = 24.dp)
        ) {
            Text(
                text = "Let's get to know you",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Personal Information Section
        Text(
            text = "Personal information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // First Name TextField with Placeholder
        LabelledTextField(
            label = "First name",
            placeholder = "Enter your first name",
            value = firstName.value
        ) { firstName.value = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Last Name TextField with Placeholder
        LabelledTextField(
            label = "Last name",
            placeholder = "Enter your last name",
            value = lastName.value
        ) { lastName.value = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField with Placeholder
        LabelledTextField(
            label = "Email",
            placeholder = "Enter your email",
            value = email.value
        ) { email.value = it }

        Spacer(modifier = Modifier.height(32.dp))

        // Register Button
        Button(
            onClick = { /* Handle Registration */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Register",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ✅ **Reusable TextField with Label & Placeholder**
@Composable
fun LabelledTextField(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = placeholder) }, // 🔹 Added Placeholder!
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    Onboarding()
}