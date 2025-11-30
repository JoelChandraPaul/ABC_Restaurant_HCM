package ca.gbc.comp3074.abc_hcm.ui.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.EmployeeViewModel
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleScreen(
    nav: NavHostController,
    empVM: EmployeeViewModel = viewModel(),
    vm: ScheduleViewModel = viewModel()
) {
    val employees = empVM.employees.observeAsState(emptyList()).value

    var selectedEmployee by remember { mutableStateOf("") }
    var dayPickerOpen by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("") }

    var startExpanded by remember { mutableStateOf(false) }
    var endExpanded by remember { mutableStateOf(false) }
    var startHour by remember { mutableStateOf<Int?>(null) }
    var endHour by remember { mutableStateOf<Int?>(null) }

    val shiftLabel = if (startHour != null && endHour != null)
        "${startHour.toString().padStart(2, '0')}:00 - ${endHour.toString().padStart(2, '0')}:00"
    else ""

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Shift", fontWeight = FontWeight.Bold) })
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            /* Employee Picker */
            ExposedDropdownMenuBox(
                expanded = selectedEmployee.isEmpty(),
                onExpandedChange = {}
            ) {
                OutlinedTextField(
                    value = selectedEmployee,
                    readOnly = true,
                    onValueChange = {},
                    label = { Text("Select Employee") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clickable {  }
                )

                ExposedDropdownMenu(
                    expanded = selectedEmployee.isEmpty(),
                    onDismissRequest = {}
                ) {
                    employees.forEach { emp ->
                        DropdownMenuItem(
                            text = { Text("${emp.name} (${emp.employeeId})") },
                            onClick = { selectedEmployee = emp.employeeId }
                        )
                    }
                }
            }

            /* Day Selector */
            OutlinedButton(
                onClick = { dayPickerOpen = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (selectedDay.isEmpty()) "Pick Day" else selectedDay) }

            if (dayPickerOpen) {
                AlertDialog(
                    onDismissRequest = { dayPickerOpen = false },
                    title = { Text("Select Day") },
                    text = {
                        Column {
                            listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday").forEach {
                                Text(it, modifier = Modifier.clickable {
                                    selectedDay = it
                                    dayPickerOpen = false
                                }.padding(10.dp))
                            }
                        }
                    },
                    confirmButton = {}
                )
            }

            /* START TIME */
            ExposedDropdownMenuBox(expanded = startExpanded, onExpandedChange = { startExpanded = true }) {
                OutlinedTextField(
                    value = startHour?.toString() ?: "",
                    readOnly = true,
                    label = { Text("Start Hour") },
                    modifier = Modifier.fillMaxWidth().menuAnchor().clickable { startExpanded = true },
                    onValueChange = {}
                )

                ExposedDropdownMenu(expanded = startExpanded, onDismissRequest = { startExpanded = false }) {
                    (0..23).forEach { hr ->
                        DropdownMenuItem(
                            text = { Text("$hr:00") },
                            onClick = { startHour = hr; startExpanded = false }
                        )
                    }
                }
            }

            /* END TIME */
            ExposedDropdownMenuBox(expanded = endExpanded, onExpandedChange = { endExpanded = true }) {
                OutlinedTextField(
                    value = endHour?.toString() ?: "",
                    readOnly = true,
                    label = { Text("End Hour") },
                    modifier = Modifier.fillMaxWidth().menuAnchor().clickable { endExpanded = true },
                    onValueChange = {}
                )

                ExposedDropdownMenu(expanded = endExpanded, onDismissRequest = { endExpanded = false }) {
                    (0..23).forEach { hr ->
                        DropdownMenuItem(
                            text = { Text("$hr:00") },
                            onClick = { endHour = hr; endExpanded = false }
                        )
                    }
                }
            }

            Button(
                enabled = selectedEmployee.isNotEmpty() && selectedDay.isNotEmpty() && startHour != null && endHour != null,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                onClick = {
                    val date = LocalDate.parse(selectedDay)  // Convert selectedDay to LocalDate
                    vm.add(date, shiftLabel, selectedEmployee)
                    nav.popBackStack()
                }
            ) { Text("Save Shift") }
        }
    }
}
