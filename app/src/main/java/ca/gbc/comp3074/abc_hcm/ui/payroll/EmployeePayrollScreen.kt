package ca.gbc.comp3074.abc_hcm.ui.payroll

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
    val p = payroll.find { it.employeeId == id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFF))
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("My Earnings", fontSize = 30.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(22.dp))

        if (p == null) {
            Text("No payroll record yet", fontSize = 18.sp)
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    Modifier.padding(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Total Earned",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )

                    Text(
                        NumberFormat.getCurrencyInstance().format(p.totalPay),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A84FF),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    Divider(Modifier.padding(vertical = 14.dp))

                    Text("Hours Worked: ${p.hoursWorked}", fontSize = 18.sp)
                    Text("Hourly Rate: \$${p.hourlyRate}", fontSize = 18.sp)
                }
            }
        }

        Spacer(Modifier.height(35.dp))

        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Back") }
    }
}
