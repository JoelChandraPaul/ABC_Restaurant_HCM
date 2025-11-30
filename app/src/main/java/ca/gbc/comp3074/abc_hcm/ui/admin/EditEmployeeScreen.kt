package ca.gbc.comp3074.abc_hcm.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.EmployeeViewModel
import kotlinx.coroutines.launch

@Composable
fun EditEmployeeScreen(
    nav: NavHostController,
    id: String,
    vm: EmployeeViewModel = viewModel()
) {
    val employees by vm.employees.observeAsState(emptyList())
    val employee = employees.firstOrNull { it.employeeId == id }

    // Load existing employee data into fields **only once**
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }

    LaunchedEffect(employee) {
        if (employee != null) {
            name = employee.name
            password = employee.password
            hourlyRate = employee.hourlyRate.toString()
        }
    }

    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(22.dp)) {

        Text("Edit Employee", fontSize = 24.sp)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(autoCorrect = false),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(autoCorrect = false),
            singleLine = true
        )

        OutlinedTextField(
            value = hourlyRate,
            onValueChange = { hourlyRate = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Hourly Rate") },
            keyboardOptions = KeyboardOptions(autoCorrect = false),
            singleLine = true
        )

        Spacer(Modifier.height(22.dp))

        Button(
            onClick = {
                scope.launch {
                    vm.updateEmployee(id, name, password, hourlyRate.toDoubleOrNull() ?: 0.0)
                    nav.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
