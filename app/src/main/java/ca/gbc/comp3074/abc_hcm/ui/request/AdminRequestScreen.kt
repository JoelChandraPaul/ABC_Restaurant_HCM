package ca.gbc.comp3074.abc_hcm.ui.request

import androidx.compose.foundation.BorderStroke
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

    // 创建员工ID到姓名的映射
    val employeeMap = remember(employees) {
        employees.associate { it.employeeId to it.name }
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Text(
            "Employee Requests Management",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        // 统计信息
        val pendingCount = requests.count { it.status == "Pending" }
        val approvedCount = requests.count { it.status == "Approved" }
        val rejectedCount = requests.count { it.status == "Rejected" }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryChip("Pending", pendingCount, Color(0xFFFF9800))
            SummaryChip("Approved", approvedCount, Color(0xFF4CAF50))
            SummaryChip("Rejected", rejectedCount, Color(0xFFF44336))
        }

        Spacer(Modifier.height(16.dp))

        if (requests.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No requests submitted yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(requests) { request ->
                    RequestCard(
                        request = request,
                        employeeName = employeeMap[request.employee] ?: "Unknown",
                        onApprove = { requestVm.updateStatus(request.id, "Approved") },
                        onReject = { requestVm.updateStatus(request.id, "Rejected") }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Dashboard")
        }
    }
}

@Composable
fun SummaryChip(label: String, count: Int, color: Color) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
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
    val (statusColor, statusIcon) = when (request.status) {
        "Approved" -> Color(0xFF4CAF50) to Icons.Default.CheckCircle
        "Rejected" -> Color(0xFFF44336) to Icons.Default.Close
        else -> Color(0xFFFF9800) to Icons.Default.Info
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (request.status) {
                "Pending" -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(Modifier.padding(16.dp)) {

            // 头部：员工信息和状态
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = employeeName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ID: ${request.employee}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 状态徽章
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = statusColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, statusColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = request.status,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            // 请假详情
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow(label = "Type", value = request.type)
                    Spacer(Modifier.height(8.dp))
                    InfoRow(label = "Date", value = request.date)
                }
            }

            Spacer(Modifier.height(8.dp))

            Column {
                Text(
                    text = "Reason:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = request.reason,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // 显示操作按钮（允许管理员随时修改状态）
            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Approve Button - filled if already approved, outlined otherwise
                if (request.status == "Approved") {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            disabledContainerColor = Color(0xFF4CAF50).copy(alpha = 0.6f),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Approved")
                    }
                } else {
                    OutlinedButton(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4CAF50)
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFF4CAF50))
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Approve")
                    }
                }

                // Reject Button - filled if already rejected, outlined otherwise
                if (request.status == "Rejected") {
                    Button(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336),
                            disabledContainerColor = Color(0xFFF44336).copy(alpha = 0.6f),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Rejected")
                    }
                } else {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFF44336)
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFFF44336))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Reject")
                    }
                }
            }

            // Add a hint for managers
            if (request.status != "Pending") {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Click ${if (request.status == "Approved") "Reject" else "Approve"} to change the status",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
