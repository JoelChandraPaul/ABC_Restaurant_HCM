package ca.gbc.comp3074.abc_hcm.ui.payroll

import androidx.compose.foundation.background
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
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.PayrollViewModel
import java.text.NumberFormat

@Composable
fun EmployeePayrollScreen(nav: NavHostController, id: String, vm: PayrollViewModel = viewModel()) {

    val payroll = vm.payroll.observeAsState(emptyList()).value
    val payrollHistory = vm.payrollHistory.observeAsState(emptyList()).value
    val p = payroll.find { it.employeeId == id }

    var showHistory by remember { mutableStateOf(false) }

    // Load payroll history for this employee
    LaunchedEffect(id) {
        vm.loadPayrollHistory(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFF))
            .padding(24.dp)
    ) {

        Text("My Earnings", fontSize = 28.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(18.dp))

        // Toggle between current pay and history
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showHistory = false },
                modifier = Modifier.weight(1f),
                colors = if (!showHistory) ButtonDefaults.buttonColors()
                else ButtonDefaults.outlinedButtonColors()
            ) {
                Text("Current Pay")
            }
            Button(
                onClick = { showHistory = true },
                modifier = Modifier.weight(1f),
                colors = if (showHistory) ButtonDefaults.buttonColors()
                else ButtonDefaults.outlinedButtonColors()
            ) {
                Text("History")
            }
        }

        Spacer(Modifier.height(18.dp))

        if (!showHistory) {
            // Current Payroll View
            if (p == null) {
                Text("No payroll record yet", fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Net Pay Summary Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A84FF)),
                            elevation = CardDefaults.cardElevation(6.dp),
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Column(
                                Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Net Pay",
                                    fontSize = 16.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    NumberFormat.getCurrencyInstance().format(p.netPay),
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                if (p.periodStart != null && p.periodEnd != null) {
                                    Text(
                                        "${p.periodStart} to ${p.periodEnd}",
                                        fontSize = 13.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }

                    // Hours Detail Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text("Hours Breakdown", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))

                                PayStubRow("Regular Hours", "%.1f hrs".format(p.regularHours))
                                PayStubRow("Overtime Hours", "%.1f hrs".format(p.overtimeHours), Color(0xFFFF6B6B))
                                Divider(Modifier.padding(vertical = 8.dp))
                                PayStubRow("Total Hours", "%.1f hrs".format(p.totalHours), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Pay Detail Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text("Pay Breakdown", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))

                                PayStubRow("Hourly Rate", NumberFormat.getCurrencyInstance().format(p.hourlyRate))
                                Spacer(Modifier.height(8.dp))
                                PayStubRow("Regular Pay", NumberFormat.getCurrencyInstance().format(p.regularPay))
                                PayStubRow("Overtime Pay (1.5x)", NumberFormat.getCurrencyInstance().format(p.overtimePay))
                                Divider(Modifier.padding(vertical = 8.dp))
                                PayStubRow("Gross Pay", NumberFormat.getCurrencyInstance().format(p.grossPay),
                                    color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Deductions Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text("Deductions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))

                                PayStubRow("Tax (15%)", "-${NumberFormat.getCurrencyInstance().format(p.taxDeduction)}",
                                    Color.Gray)
                                PayStubRow("Insurance (5%)", "-${NumberFormat.getCurrencyInstance().format(p.insuranceDeduction)}",
                                    Color.Gray)
                                Divider(Modifier.padding(vertical = 8.dp))
                                PayStubRow("Total Deductions", "-${NumberFormat.getCurrencyInstance().format(p.totalDeductions)}",
                                    Color.Red, FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // Payroll History View
            if (payrollHistory.isEmpty()) {
                Text("No payroll history yet", fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(payrollHistory) { history ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(Modifier.padding(18.dp)) {
                                Text("Paid on ${history.calculatedDate}",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold)

                                Spacer(Modifier.height(10.dp))

                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Net Pay:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(
                                        NumberFormat.getCurrencyInstance().format(history.netPay),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF0A84FF)
                                    )
                                }

                                Spacer(Modifier.height(8.dp))
                                Divider()
                                Spacer(Modifier.height(8.dp))

                                PayStubRow("Hours", "%.1f (%.1f OT)".format(
                                    history.regularHours + history.overtimeHours, history.overtimeHours),
                                    fontSize = 14.sp)
                                PayStubRow("Gross Pay", NumberFormat.getCurrencyInstance().format(history.grossPay),
                                    fontSize = 14.sp)
                                PayStubRow("Deductions", "-${NumberFormat.getCurrencyInstance().format(history.totalDeductions)}",
                                    Color.Gray, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Back button
        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Back") }
    }
}

@Composable
fun PayStubRow(
    label: String,
    value: String,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: androidx.compose.ui.unit.TextUnit = 15.sp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = fontSize, color = color, fontWeight = fontWeight)
        Text(value, fontSize = fontSize, color = color, fontWeight = fontWeight)
    }
}
