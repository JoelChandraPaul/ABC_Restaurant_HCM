package ca.gbc.comp3074.abc_hcm.ui.payroll

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ca.gbc.comp3074.abc_hcm.viewmodel.PayrollViewModel
import java.text.NumberFormat

@Composable
fun AdminPayrollScreen(nav: NavController, vm: PayrollViewModel = viewModel()) {

    val payroll = vm.payroll.observeAsState(emptyList()).value
    val payPeriods = vm.payPeriods.observeAsState(emptyList()).value
    val selectedPeriod = vm.selectedPayPeriod.observeAsState(null).value

    var showCreatePeriodDialog by remember { mutableStateOf(false) }
    var expandedPeriod by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(22.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Payroll Summary",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // Pay Period Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dropdown for pay period selection
            Box(modifier = Modifier.weight(1f)) {
                OutlinedButton(
                    onClick = { expandedPeriod = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = selectedPeriod?.let { "${it.periodType}: ${it.startDate} to ${it.endDate}" }
                            ?: "All Time",
                        fontSize = 14.sp
                    )
                }
                DropdownMenu(
                    expanded = expandedPeriod,
                    onDismissRequest = { expandedPeriod = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All Time") },
                        onClick = {
                            vm.selectPayPeriod(null)
                            expandedPeriod = false
                        }
                    )
                    payPeriods.forEach { period ->
                        DropdownMenuItem(
                            text = { Text("${period.periodType}: ${period.startDate} to ${period.endDate}") },
                            onClick = {
                                vm.selectPayPeriod(period)
                                expandedPeriod = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // Create Period Button
            Button(onClick = { showCreatePeriodDialog = true }) {
                Text("+ Period")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Save to History Button (only show if period is selected)
        if (selectedPeriod != null && !selectedPeriod.isProcessed) {
            Button(
                onClick = { vm.savePayrollToHistory(selectedPeriod) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Save Payroll to History")
            }
            Spacer(Modifier.height(12.dp))
        }

        // Display payroll data
        if (payroll.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("Loading payroll data...", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(payroll) { p ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        shape = MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(18.dp)) {

                            Text("${p.employeeName}  •  ${p.employeeId}",
                                fontWeight = FontWeight.Bold, fontSize = 18.sp)

                            Spacer(Modifier.height(10.dp))

                            // Hours breakdown
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Regular Hours:", fontSize = 14.sp)
                                Text("%.1f hrs @ \$%.2f".format(p.regularHours, p.hourlyRate), fontSize = 14.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Overtime Hours:", fontSize = 14.sp, color = Color(0xFFFF6B6B))
                                Text("%.1f hrs @ \$%.2f".format(p.overtimeHours, p.hourlyRate * 1.5), fontSize = 14.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Hours:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("%.1f hrs".format(p.totalHours), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(Modifier.height(8.dp))
                            Divider()
                            Spacer(Modifier.height(8.dp))

                            // Pay breakdown
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Regular Pay:", fontSize = 14.sp)
                                Text(NumberFormat.getCurrencyInstance().format(p.regularPay), fontSize = 14.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Overtime Pay:", fontSize = 14.sp)
                                Text(NumberFormat.getCurrencyInstance().format(p.overtimePay), fontSize = 14.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Gross Pay:", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(NumberFormat.getCurrencyInstance().format(p.grossPay),
                                    fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(Modifier.height(8.dp))
                            Divider()
                            Spacer(Modifier.height(8.dp))

                            // Deductions
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Tax (15%):", fontSize = 14.sp, color = Color.Gray)
                                Text("-${NumberFormat.getCurrencyInstance().format(p.taxDeduction)}",
                                    fontSize = 14.sp, color = Color.Gray)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Insurance (5%):", fontSize = 14.sp, color = Color.Gray)
                                Text("-${NumberFormat.getCurrencyInstance().format(p.insuranceDeduction)}",
                                    fontSize = 14.sp, color = Color.Gray)
                            }

                            Spacer(Modifier.height(8.dp))
                            Divider()

                            // Net Pay
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Net Pay:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(
                                    NumberFormat.getCurrencyInstance().format(p.netPay),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF0077CC)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Back button
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { nav.popBackStack() }
        ) { Text("Back") }
    }

    // Create Pay Period Dialog
    if (showCreatePeriodDialog) {
        CreatePayPeriodDialog(
            onDismiss = { showCreatePeriodDialog = false },
            onConfirm = { startDate, endDate, periodType ->
                vm.createPayPeriod(startDate, endDate, periodType)
                showCreatePeriodDialog = false
            }
        )
    }
}

@Composable
fun CreatePayPeriodDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var periodType by remember { mutableStateOf("WEEKLY") }
    var expandedType by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Pay Period") },
        text = {
            Column {
                Text("Period Type:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Box {
                    OutlinedButton(
                        onClick = { expandedType = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(periodType)
                    }
                    DropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("WEEKLY") },
                            onClick = { periodType = "WEEKLY"; expandedType = false }
                        )
                        DropdownMenuItem(
                            text = { Text("MONTHLY") },
                            onClick = { periodType = "MONTHLY"; expandedType = false }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text("Start Date (yyyy-MM-dd):", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    placeholder = { Text("2024-01-01") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text("End Date (yyyy-MM-dd):", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                TextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    placeholder = { Text("2024-01-07") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(startDate, endDate, periodType) },
                enabled = startDate.isNotBlank() && endDate.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
