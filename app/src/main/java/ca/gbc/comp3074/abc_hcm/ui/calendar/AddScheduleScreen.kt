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

    var empExpanded by remember { mutableStateOf(false) }
    var startExpanded by remember { mutableStateOf(false) }
    var endExpanded by remember { mutableStateOf(false) }

    var startHour by remember { mutableStateOf<Int?>(null) }
    var endHour by remember { mutableStateOf<Int?>(null) }

    val shiftLabel =
        if (startHour != null && endHour != null)
            "${startHour.toString().padStart(2, '0')}:00 - ${endHour.toString().padStart(2, '0')}:00"
        else ""

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text("Create Schedule", style = MaterialTheme.typography.headlineMedium)

        ExposedDropdownMenuBox(expanded = empExpanded, onExpandedChange = { empExpanded = true }) {
            OutlinedTextField(
                value = selected,
                readOnly = true,
                onValueChange = {},
                label = { Text("Select Employee") },
                modifier = Modifier.fillMaxWidth().menuAnchor().clickable { empExpanded = true }
            )

            ExposedDropdownMenu(expanded = empExpanded, onDismissRequest = { empExpanded = false }) {
                employees.forEach { e ->
                    DropdownMenuItem(
                        text = { Text("${e.name} (${e.employeeId})") },
                        onClick = {
                            selected = e.employeeId
                            empExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = day,
            onValueChange = { day = it },
            label = { Text("Day (ex. Monday)") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(expanded = startExpanded, onExpandedChange = { startExpanded = true }) {
            OutlinedTextField(
                value = startHour?.toString() ?: "",
                readOnly = true,
                onValueChange = {},
                label = { Text("Start Time (24hr)") },
                modifier = Modifier.fillMaxWidth().menuAnchor().clickable { startExpanded = true }
            )

            ExposedDropdownMenu(expanded = startExpanded, onDismissRequest = { startExpanded = false }) {
                (0..23).forEach { h ->
                    DropdownMenuItem(
                        text = { Text("${h}:00") },
                        onClick = {
                            startHour = h
                            startExpanded = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(expanded = endExpanded, onExpandedChange = { endExpanded = true }) {
            OutlinedTextField(
                value = endHour?.toString() ?: "",
                readOnly = true,
                onValueChange = {},
                label = { Text("End Time (24hr)") },
                modifier = Modifier.fillMaxWidth().menuAnchor().clickable { endExpanded = true }
            )

            ExposedDropdownMenu(expanded = endExpanded, onDismissRequest = { endExpanded = false }) {
                (0..23).forEach { h ->
                    DropdownMenuItem(
                        text = { Text("${h}:00") },
                        onClick = {
                            endHour = h
                            endExpanded = false
                        }
                    )
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (selected.isNotEmpty() && startHour != null && endHour != null && day.isNotEmpty()) {
                    vm.add(day, shiftLabel, selected)
                    nav.popBackStack()
                }
            }
        ) {
            Text("Save Schedule")
        }
    }
}
