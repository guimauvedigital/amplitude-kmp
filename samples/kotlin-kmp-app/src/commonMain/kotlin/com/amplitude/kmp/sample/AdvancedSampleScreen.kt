package com.amplitude.kmp.sample

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amplitude.kmp.events.Identify
import com.amplitude.kmp.events.Revenue
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSampleScreen(onBack: () -> Unit) {
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Advanced Features") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Advanced Amplitude Features",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = {
                    // Set user properties using Identify
                    val identify = Identify()
                        .set("user_platform", getPlatformName())
                        .set("custom_property", "sample_value")
                        .set("is_kmp_user", true)

                    AmplitudeManager.amplitude.identify(identify)
                    message = "✓ User properties set"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Set User Properties (Identify)")
            }

            Button(
                onClick = {
                    // Set groups
                    AmplitudeManager.amplitude.setGroup("orgId", "15")
                    AmplitudeManager.amplitude.setGroup(
                        groupType = "platform",
                        groupName = arrayOf(getPlatformName(), "KMP")
                    )
                    message = "✓ Groups set"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text("Set Groups")
            }

            Button(
                onClick = {
                    // Group identify
                    val groupIdentifyObj = Identify()
                        .set("plan_type", "premium")
                        .set("trial_days", 30)

                    AmplitudeManager.amplitude.groupIdentify(
                        groupType = "orgId",
                        groupName = "15",
                        identify = groupIdentifyObj
                    )
                    message = "✓ Group properties set"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                )
            ) {
                Text("Set Group Properties")
            }

            Button(
                onClick = {
                    // Track revenue
                    val revenue = Revenue().apply {
                        productId = "com.amplitude.product"
                        price = 9.99
                        quantity = 1
                        revenueType = "purchase"
                        properties = mutableMapOf(
                            "category" to "subscription",
                            "platform" to getPlatformName()
                        )
                    }

                    AmplitudeManager.amplitude.revenue(revenue)
                    message = "✓ Revenue tracked: $9.99"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )
            ) {
                Text("Track Revenue")
            }

            Button(
                onClick = {
                    // Send all advanced events at once
                    sendAllAdvancedEvents()
                    message = "✓ All advanced events sent!"
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                )
            ) {
                Text("Send All Advanced Events")
            }
        }
    }
}

private fun sendAllAdvancedEvents() {
    // Set user properties
    val identify = Identify()
        .set("user_platform", getPlatformName())
        .set("custom_property", "batch_test")
    AmplitudeManager.amplitude.identify(identify)

    // Set groups
    val groupType = "test-group-type"
    val groupName = "kmp-sample"
    AmplitudeManager.amplitude.setGroup(groupType, groupName)
    AmplitudeManager.amplitude.setGroup("orgId", "15")

    // Group identify
    val groupIdentifyObj = Identify()
        .set("key", "value")
        .set("timestamp", Clock.System.now().toEpochMilliseconds())
    AmplitudeManager.amplitude.groupIdentify(groupType, groupName, groupIdentifyObj)

    // Track revenue
    val revenue = Revenue().apply {
        productId = "com.amplitude.batch_product"
        price = 19.99
        quantity = 2
    }
    AmplitudeManager.amplitude.revenue(revenue)
}
