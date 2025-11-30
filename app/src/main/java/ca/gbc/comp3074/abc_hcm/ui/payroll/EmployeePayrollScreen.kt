package ca.gbc.comp3074.abc_hcm.ui.payroll

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.PayrollViewModel
import java.text.NumberFormat

@Composable
fun EmployeePayrollScreen(nav: NavHostController, id: String, vm: PayrollViewModel = viewModel()) {

    val payroll = vm.payroll.observeAsState(emptyList()).value
    val data = payroll.find { it.employeeId == id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("My Payroll", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(20.dp))

        if (data == null) {
            Text("No payroll records found.")
        } else {
            Text("Hours Worked: ${data.hoursWorked}")
            Text("Hourly Rate: \$${data.hourlyRate}")
            Text("Total Earned: ${NumberFormat.getCurrencyInstance().format(data.totalPay)}")
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Back") }
    }
}
