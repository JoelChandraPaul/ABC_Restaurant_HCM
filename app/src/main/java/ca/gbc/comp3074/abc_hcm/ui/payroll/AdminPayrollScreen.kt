package ca.gbc.comp3074.abc_hcm.ui.payroll

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ca.gbc.comp3074.abc_hcm.viewmodel.PayrollViewModel
import java.text.NumberFormat

@Composable
fun AdminPayrollScreen(nav: NavController, vm: PayrollViewModel = viewModel()) {

    val payroll = vm.payroll.observeAsState(emptyList()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Payroll Summary", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(15.dp))

        LazyColumn {
            items(payroll) { p ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text("Employee: ${p.employeeName} (${p.employeeId})")
                        Text("Hours Worked: ${p.hoursWorked}")
                        Text("Hourly Rate: \$${p.hourlyRate}")
                        Text("Total Pay: ${NumberFormat.getCurrencyInstance().format(p.totalPay)}")
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Back") }
    }
}
