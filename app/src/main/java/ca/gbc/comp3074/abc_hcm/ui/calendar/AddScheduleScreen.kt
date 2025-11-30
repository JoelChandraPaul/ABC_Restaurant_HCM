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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(
    nav: NavHostController,
    empVM: EmployeeViewModel = viewModel(),
    vm: ScheduleViewModel = viewModel()
) {
    val employees = empVM.employees.observeAsState(emptyList()).value

    var selected by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }
    var shiftLabel by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

        Text("Add New Shift", style = MaterialTheme.typography.headlineMedium)

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = true }) {

            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Employee") },
                modifier = Modifier.fillMaxWidth().menuAnchor().clickable { expanded = true }
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                employees.forEach { e ->
                    DropdownMenuItem(
                        text = { Text("${e.name} (${e.employeeId})") },
                        onClick = {
                            selected = e.employeeId
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(day, { day = it }, label = { Text("Day (e.g. Monday)") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(hours, { hours = it }, label = { Text("Hours Worked (e.g. 8)") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(shiftLabel, { shiftLabel = it }, label = { Text("Shift Label (ex. 9am-5pm)") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                if (selected.isNotEmpty() && hours.isNotEmpty() && day.isNotEmpty()) {
                    vm.add(selected, day, hours.toInt(), shiftLabel)
                    nav.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save Shift") }
    }
}
