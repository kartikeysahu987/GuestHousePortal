package com.example.guesthouesportal1

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Data model for a dining order, following your schema
data class DiningOrder(
    val orderId: String = "",
    val userId: String = "",
    val date: String = "",
    // mealType will be a comma-separated list like "Breakfast: 4, Dinner: 5"
    val mealType: String = "",
    // quantity is the total number of people across all meals
    val quantity: Int = 0,
    val totalPrice: Int = 0,
    val timestamp: Long = 0L
)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiningScreen(onNavigateUp: () -> Unit) {
    // Date selection: "Today" or "Tomorrow"
    var dateOption by remember { mutableStateOf("Today") }
    val todayDate = LocalDate.now()
    val tomorrowDate = todayDate.plusDays(1)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = if (dateOption == "Today") todayDate.format(formatter) else tomorrowDate.format(formatter)

    // Always show all three meal rows.
    // For each meal, maintain its quantity as a string.
    var mealQuantities by remember {
        mutableStateOf(mapOf("breakfast" to "", "lunch" to "", "dinner" to ""))
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    // Define prices per meal type.
    val mealPrices = mapOf("breakfast" to 50, "lunch" to 70, "dinner" to 70)

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        "Dining Reservation",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                modifier = Modifier
                    .shadow(4.dp)
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shadowElevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .imePadding()
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Header Title
                Text(
                    text = "Book Your Dining",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                // Date option selection
                Text(text = "Select Date:", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DateOptionRadioButton(
                        modifier = Modifier.weight(1f),
                        selectedOption = dateOption,
                        option = "Today"
                    ) { dateOption = it }
                    DateOptionRadioButton(
                        modifier = Modifier.weight(1f),
                        selectedOption = dateOption,
                        option = "Tomorrow"
                    ) { dateOption = it }
                }
                Text(text = "Date: $date", style = MaterialTheme.typography.bodyMedium)

                // Now, always display three lines—one each for Breakfast, Lunch, and Dinner.
                MealQuantityRow(
                    meal = "breakfast",
                    price = mealPrices["breakfast"] ?: 0,
                    quantity = mealQuantities["breakfast"] ?: "",
                    onQuantityChange = { newValue ->
                        mealQuantities = mealQuantities.toMutableMap().apply { put("breakfast", newValue) }
                    }
                )
                MealQuantityRow(
                    meal = "lunch",
                    price = mealPrices["lunch"] ?: 0,
                    quantity = mealQuantities["lunch"] ?: "",
                    onQuantityChange = { newValue ->
                        mealQuantities = mealQuantities.toMutableMap().apply { put("lunch", newValue) }
                    }
                )
                MealQuantityRow(
                    meal = "dinner",
                    price = mealPrices["dinner"] ?: 0,
                    quantity = mealQuantities["dinner"] ?: "",
                    onQuantityChange = { newValue ->
                        mealQuantities = mealQuantities.toMutableMap().apply { put("dinner", newValue) }
                    }
                )

                // Animated error message
                AnimatedVisibility(visible = errorMessage != null, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Compute totals from all three meals.
                val totalPeople = listOf("breakfast", "lunch", "dinner").sumOf { meal ->
                    mealQuantities[meal]?.toIntOrNull() ?: 0
                }
                val totalPrice = listOf("breakfast", "lunch", "dinner").sumOf { meal ->
                    (mealQuantities[meal]?.toIntOrNull() ?: 0) * (mealPrices[meal] ?: 0)
                }

                // Bill Summary Section (if at least one valid quantity is entered)
                if (totalPeople > 0) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Bill Summary",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            listOf("breakfast", "lunch", "dinner").forEach { meal ->
                                val qty = mealQuantities[meal]?.toIntOrNull() ?: 0
                                if (qty > 0) {
                                    val mealName = meal.replaceFirstChar { it.uppercase() }
                                    Text(
                                        text = "$mealName: $qty x Rs${mealPrices[meal]} = Rs${qty * (mealPrices[meal] ?: 0)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Total People: $totalPeople",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Total Price: Rs$totalPrice",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                // Book Dining button: sends data directly to Firebase
                Button(
                    onClick = {
                        keyboardController?.hide()
                        // Validate that at least one meal has a valid positive quantity.
                        val isAnyMealOrdered = listOf("breakfast", "lunch", "dinner").any { meal ->
                            val qty = mealQuantities[meal]?.toIntOrNull() ?: 0
                            qty > 0
                        }
                        if (!isAnyMealOrdered) {
                            errorMessage = "Please enter valid quantities for at least one meal."
                            return@Button
                        }
                        // Compute totals again.
                        val totalPeopleCheck = listOf("breakfast", "lunch", "dinner").sumOf { meal ->
                            mealQuantities[meal]?.toIntOrNull() ?: 0
                        }
                        val computedTotalPrice = listOf("breakfast", "lunch", "dinner").sumOf { meal ->
                            (mealQuantities[meal]?.toIntOrNull() ?: 0) * (mealPrices[meal] ?: 0)
                        }
                        errorMessage = null

                        // Get current user id
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser == null) {
                            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val userId = currentUser.uid

                        // Prepare order details as a comma-separated string for meals with quantity > 0.
                        val mealDetails = listOf("breakfast", "lunch", "dinner")
                            .filter { meal -> (mealQuantities[meal]?.toIntOrNull() ?: 0) > 0 }
                            .joinToString(", ") { meal ->
                                "${meal.replaceFirstChar { it.uppercase() }}: ${mealQuantities[meal]}"
                            }

                        val order = DiningOrder(
                            orderId = "",
                            userId = userId,
                            date = date,
                            mealType = mealDetails,
                            quantity = totalPeopleCheck,
                            totalPrice = computedTotalPrice,
                            timestamp = System.currentTimeMillis() / 1000L
                        )
                        val databaseRef = FirebaseDatabase.getInstance().getReference("DiningOrders")
                        val newOrderRef = databaseRef.push()
                        val orderId = newOrderRef.key ?: ""
                        newOrderRef.setValue(order.copy(orderId = orderId))
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Booking successful!\n$mealDetails\nDate: $date\nTotal People: $totalPeopleCheck\nTotal Price: Rs$computedTotalPrice",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error: ${task.exception?.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        onNavigateUp()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Book Dining")
                }
            }
        }
    }
}

@Composable
fun MealQuantityRow(
    meal: String,
    price: Int,
    quantity: String,
    onQuantityChange: (String) -> Unit
) {
    // This Row displays: "Breakfast (Rs50):" followed by an input field.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val mealLabel = meal.replaceFirstChar { it.uppercase() }
        Text(
            text = "$mealLabel (Rs$price):",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(150.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = onQuantityChange,
            placeholder = { Text("e.g. 2") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DateOptionRadioButton(
    modifier: Modifier = Modifier,
    selectedOption: String,
    option: String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedOption == option,
            onClick = { onOptionSelected(option) }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = option,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
