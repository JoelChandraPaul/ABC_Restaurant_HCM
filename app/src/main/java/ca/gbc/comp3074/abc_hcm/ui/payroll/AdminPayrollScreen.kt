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

        Spacer(Modifier.height(20.dp))

        // Display a loading indicator if the payroll data is empty
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

                            Spacer(Modifier.height(6.dp))

                            Text("Hours Worked: ${p.hoursWorked}")
                            Text("Hourly Rate: \$${p.hourlyRate}")

                            Spacer(Modifier.height(8.dp))
                            Divider()

                            Text(
                                "Total Pay:  ${NumberFormat.getCurrencyInstance().format(p.totalPay)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF0077CC),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Back button to navigate back
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { nav.popBackStack() }
        ) { Text("Back") }
    }
}
