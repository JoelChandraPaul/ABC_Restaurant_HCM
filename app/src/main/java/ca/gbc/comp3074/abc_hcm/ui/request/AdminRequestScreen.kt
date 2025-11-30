package ca.gbc.comp3074.abc_hcm.ui.request

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.EmployeeViewModel
import ca.gbc.comp3074.abc_hcm.viewmodel.RequestViewModel

@Composable
fun AdminRequestScreen(
    nav: NavHostController,
    requestVm: RequestViewModel = viewModel(),
    employeeVm: EmployeeViewModel = viewModel()
) {

    val requests = requestVm.requests.observeAsState(emptyList()).value
    val employees = employeeVm.employees.observeAsState(emptyList()).value
    val employeeMap = employees.associate { it.employeeId to it.name }

    var selectedTab by remember { mutableStateOf("Pending") }

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Text("Employee Requests Management",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        val pending = requests.filter { it.status == "Pending" }
        val approved = requests.filter { it.status == "Approved" }
        val rejected = requests.filter { it.status == "Rejected" }

        // 🔥 FILTER TABS
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            FilterChip("Pending", pending.size, Color(0xFFFF9800), selectedTab == "Pending") { selectedTab = "Pending" }
            FilterChip("Approved", approved.size, Color(0xFF4CAF50), selectedTab == "Approved") { selectedTab = "Approved" }
            FilterChip("Rejected", rejected.size, Color(0xFFF44336), selectedTab == "Rejected") { selectedTab = "Rejected" }
        }

        Spacer(Modifier.height(16.dp))

        val listToShow = when(selectedTab) {
            "Approved" -> approved
            "Rejected" -> rejected
            else -> pending
        }

        if (listToShow.isEmpty()) {
            NoResultMessage("No $selectedTab requests")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listToShow) { req ->
                    RequestCard(
                        request = req,
                        employeeName = employeeMap[req.employee] ?: "Unknown",
                        onApprove = { requestVm.updateStatus(req.id, "Approved") },
                        onReject = { requestVm.updateStatus(req.id, "Rejected") }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { nav.popBackStack() }, Modifier.fillMaxWidth()) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
fun FilterChip(label: String, count: Int, color: Color, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        color = if (selected) color else color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = if (selected) Color.White else color)
            Spacer(Modifier.width(6.dp))
            Text(count.toString(), fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else color)
        }
    }
}

@Composable
fun NoResultMessage(text: String) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)) {
        Box(Modifier.padding(28.dp), contentAlignment = Alignment.Center) {
            Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
@Composable
fun RequestCard(
    request: ca.gbc.comp3074.abc_hcm.data.Request,
    employeeName: String,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val (statusColor, statusIcon) = when(request.status) {
        "Approved" -> Color(0xFF4CAF50) to Icons.Default.CheckCircle
        "Rejected" -> Color(0xFFF44336) to Icons.Default.Close
        else -> Color(0xFFFF9800) to Icons.Default.Info
    }

    Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(16.dp)) {

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column {
                    Text(employeeName, fontWeight = FontWeight.Bold)
                    Text("ID: ${request.employee}", color = Color.Gray)
                }

                Surface(color = statusColor.copy(.15f), border = BorderStroke(1.dp, statusColor)) {
                    Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(statusIcon, null, tint = statusColor)
                        Spacer(Modifier.width(4.dp))
                        Text(request.status, color = statusColor)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))

            Text("Type: ${request.type}")
            Text("Date: ${request.date}")
            Text("Reason: ${request.reason}")

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                OutlinedButton(onClick = onApprove, enabled = request.status != "Approved") {
                    Text("Approve")
                }
                OutlinedButton(onClick = onReject, enabled = request.status != "Rejected") {
                    Text("Reject")
                }
            }
        }
    }
}
