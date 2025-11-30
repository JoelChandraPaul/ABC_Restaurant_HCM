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
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

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
            ) { Text(selectedDate?.toString() ?: "Pick Date (yyyy-MM-dd)") }

            if (dayPickerOpen) {
                var dateInput by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { dayPickerOpen = false },
                    title = { Text("Select Date") },
                    text = {
                        Column {
                            Text("Enter date in format: yyyy-MM-dd")
                            Spacer(Modifier.height(8.dp))
                            TextField(
                                value = dateInput,
                                onValueChange = { dateInput = it },
                                placeholder = { Text("2025-11-30") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(12.dp))
                            Text("Quick Select:", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            // Quick select buttons for the next 7 days
                            (0..6).forEach { daysAhead ->
                                val date = LocalDate.now().plusDays(daysAhead.toLong())
                                Text(
                                    text = "$date (${date.dayOfWeek})",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedDate = date
                                            dayPickerOpen = false
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                try {
                                    selectedDate = LocalDate.parse(dateInput)
                                    dayPickerOpen = false
                                } catch (e: Exception) {
                                    // Invalid date format, show error or do nothing
                                }
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { dayPickerOpen = false }) {
                            Text("Cancel")
                        }
                    }
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
                enabled = selectedEmployee.isNotEmpty() && selectedDate != null && startHour != null && endHour != null,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                onClick = {
                    selectedDate?.let { date ->
                        vm.add(date, shiftLabel, selectedEmployee)
                        nav.popBackStack()
                    }
                }
            ) { Text("Save Shift") }
        }
    }
}
