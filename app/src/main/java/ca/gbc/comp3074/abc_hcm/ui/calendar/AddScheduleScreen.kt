package ca.gbc.comp3074.abc_hcm.ui.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.EmployeeViewModel
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)   // <<< FIX HERE
@Composable
fun AddScheduleScreen(
    nav: NavHostController,
    vm: ScheduleViewModel = viewModel(),
    empVM: EmployeeViewModel = viewModel()
) {
    val employees by empVM.employees.observeAsState(emptyList())

    var selectedEmployee by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var day by remember { mutableStateOf("") }
    var shift by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Text("Create Schedule", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        @OptIn(ExperimentalMaterial3Api::class)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = true }
        ) {
            OutlinedTextField(
                value = selectedEmployee,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Employee") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = true }   // <<< The fix
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                employees.forEach { emp ->
                    DropdownMenuItem(
                        text = { Text("${emp.name} (${emp.employeeId})") },
                        onClick = {
                            selectedEmployee = emp.employeeId
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        OutlinedTextField(day, { day = it }, label = { Text("Day") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(shift, { shift = it }, label = { Text("Shift (e.g., 9AM - 5PM)") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                if (selectedEmployee.isNotEmpty() && day.isNotEmpty() && shift.isNotEmpty()) {
                    vm.add(day, shift, selectedEmployee)
                    nav.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Schedule")
        }
    }
}
