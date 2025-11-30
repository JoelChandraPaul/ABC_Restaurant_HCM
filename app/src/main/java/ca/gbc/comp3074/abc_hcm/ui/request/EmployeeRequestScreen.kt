package ca.gbc.comp3074.abc_hcm.ui.request

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.gbc.comp3074.abc_hcm.viewmodel.RequestViewModel
import ca.gbc.comp3074.abc_hcm.data.Request

@Composable
fun EmployeeRequestScreen(nav: NavHostController, employeeId: String, vm: RequestViewModel = viewModel()) {

    var type by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Text("Submit Leave Request", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(18.dp))

        OutlinedTextField(type, { type = it }, label = { Text("Leave Type (Sick/Personal)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(date, { date = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(reason, { reason = it }, label = { Text("Reason") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                if(type.isNotEmpty() && date.isNotEmpty()) {
                    vm.addRequest(Request(employee = employeeId, type = type, date = date, reason = reason))
                    nav.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Submit Request") }
    }
}
