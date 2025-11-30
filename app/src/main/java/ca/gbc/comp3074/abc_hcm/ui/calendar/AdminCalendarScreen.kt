package ca.gbc.comp3074.abc_hcm.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.data.Schedule
import ca.gbc.comp3074.abc_hcm.viewmodel.EmployeeViewModel
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel
import java.time.LocalDate
import java.time.YearMonth

// ---------------------------------------------------------------------
// CALENDAR SCREEN
// ---------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCalendarScreen(
    nav: NavHostController,
    vm: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    empVM: EmployeeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val schedules = vm.schedules.collectAsState(initial = emptyList()).value
    val scheduledDates = schedules.map { it.date }.toSet()

    var month by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var openSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Schedule", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedDate = LocalDate.now()
                openSheet = true
            }) { Icon(Icons.Default.Add, contentDescription = null) }
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Month Navigation Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { month = month.minusMonths(1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous Month",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Text(
                        text = "${month.month} ${month.year}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    IconButton(onClick = { month = month.plusMonths(1) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next Month",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Days of the Week Header (Monday to Sunday)
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            val firstDayOfMonth = month.atDay(1)
            // dayOfWeek.value: Monday=1, Tuesday=2, ..., Sunday=7
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

            val days = mutableListOf<LocalDate>()

            // Add empty days at the start to align with Monday (1-indexed, so subtract 1)
            for (i in 1 until firstDayOfWeek) {
                days.add(LocalDate.MIN)
            }
            // Add all the actual days of the month
            for (i in 1..month.lengthOfMonth()) {
                days.add(month.atDay(i))
            }

            // Add empty days at the end to fill the last row if needed
            while (days.size % 7 != 0) {
                days.add(LocalDate.MIN)
            }

            // Grid for days
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(days) { date ->

                    if (date == LocalDate.MIN) {
                        // Empty spacer for alignment
                        Box(modifier = Modifier.aspectRatio(1f))
                        return@items
                    }

                    val isShiftDay = scheduledDates.contains(date.toString())
                    val isToday = date == LocalDate.now()

                    // Color scheme
                    val backgroundColor = when {
                        isToday && isShiftDay -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        isToday -> MaterialTheme.colorScheme.secondaryContainer
                        isShiftDay -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val textColor = when {
                        isToday -> MaterialTheme.colorScheme.primary
                        isShiftDay -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                selectedDate = date
                                openSheet = true
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isShiftDay || isToday) 4.dp else 2.dp
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${date.dayOfMonth}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.SemiBold,
                                color = textColor
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = if (isShiftDay) "View" else "Add",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isShiftDay) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                fontWeight = if (isShiftDay) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }

    if (selectedDate != null && openSheet)
        DayScheduleSheet(selectedDate!!, onClose = { openSheet = false }, vm, empVM)
}



// ---------------------------------------------------------------------
// VIEW + EDIT + ADD SHIFTS
// ---------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScheduleSheet(
    date: LocalDate,
    onClose: () -> Unit,
    vm: ScheduleViewModel,
    empVM: EmployeeViewModel
) {
    val employees = empVM.employees.observeAsState(emptyList()).value
    val shifts = vm.getByDate(date).collectAsState(initial = emptyList()).value

    var employee by remember { mutableStateOf("") }
    var start by remember { mutableStateOf<Int?>(null) }
    var end by remember { mutableStateOf<Int?>(null) }
    var showEmpMenu by remember { mutableStateOf(false) }
    var selectedShift by remember { mutableStateOf<Schedule?>(null) }
    var editingMode by remember { mutableStateOf(false) }

    val shift = if (start != null && end != null) "$start:00 - $end:00" else ""

    ModalBottomSheet(onDismissRequest = onClose) {

        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

            Text("Shifts — ${date.dayOfWeek}, ${date.month} ${date.dayOfMonth}, ${date.year}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)

            // Existing shifts
            shifts.forEach { s ->
                val emp = employees.find { it.employeeId == s.employee }

                Card(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${emp?.name ?: "Unknown"} (${emp?.employeeId})", fontWeight = FontWeight.Bold)
                            Text(s.shift)
                        }

                        Row {
                            // Edit Button
                            TextButton(
                                onClick = {
                                    selectedShift = s
                                    employee = s.employee
                                    // Safely split the shift time to extract hours
                                    try {
                                        val times = s.shift.split(" - ")
                                        start = times[0].substringBefore(":").toIntOrNull()
                                        end = times[1].substringBefore(":").toIntOrNull()
                                    } catch (e: Exception) {
                                        // Handle error gracefully if shift format is wrong
                                        start = null
                                        end = null
                                    }
                                    editingMode = true
                                }
                            ) { Text("Edit") }

                            // Delete Button
                            TextButton(onClick = { vm.delete(s.id) }) {
                                Text("Delete", color = Color.Red)
                            }
                        }
                    }
                }
            }

            Divider()

            // Edit Mode for selected shift
            if (editingMode) {
                Text("Edit Shift", fontWeight = FontWeight.Bold)

                // Employee Selector Dropdown for Edit mode
                EmployeeSelector(employee, employees, showEmpMenu) { sel ->
                    employee = sel
                    showEmpMenu = false
                }

                TimePicker("Start Time", start) { start = it }
                TimePicker("End Time", end) { end = it }

                Button(
                    enabled = employee.isNotEmpty() && start != null && end != null,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    onClick = {
                        if (selectedShift != null) {
                            vm.update(selectedShift!!.id, date.toString(), shift, employee)
                        }
                        onClose()
                    }
                ) { Text("Save Changes") }

                // Cancel Edit Button
                TextButton(
                    onClick = {
                        editingMode = false  // Reset to add new employee mode
                        employee = ""  // Reset employee selection
                        start = null  // Reset start time
                        end = null  // Reset end time
                    },
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text("Cancel Edit", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }
                )
            }

            // Add New Shift
            if (!editingMode) {
                Text("Add New Shift", fontWeight = FontWeight.Bold)

                // Employee Selector Dropdown for Add mode
                EmployeeSelector(employee, employees, showEmpMenu) { sel ->
                    employee = sel
                    showEmpMenu = false
                }

                TimePicker("Start Time", start) { start = it }
                TimePicker("End Time", end) { end = it }

                Button(
                    enabled = employee.isNotEmpty() && start != null && end != null,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    onClick = {
                        vm.add(date, shift, employee)
                        onClose()
                    }
                ) { Text("Save Shift") }
            }
        }
    }
}





// ---------------------------------------------------------------------
// Reusable Picker Components
// ---------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmployeeSelector(
    value: String,
    list: List<ca.gbc.comp3074.abc_hcm.data.Employee>,
    expanded: Boolean,
    onSelect: (String) -> Unit
) {
    var open by remember { mutableStateOf(expanded) }

    ExposedDropdownMenuBox(expanded = open, onExpandedChange = { open = it }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Employee") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clickable { open = !open }
        )

        ExposedDropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            list.forEach {
                DropdownMenuItem(
                    text = { Text("${it.name} (${it.employeeId})") },
                    onClick = {
                        onSelect(it.employeeId)
                        open = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePicker(label:String, value:Int?, onPick:(Int)->Unit) {

    var open by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(open, { open = it }) {
        OutlinedTextField(
            value = value?.toString() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(open, { open = false }) {
            (0..23).forEach {
                DropdownMenuItem(text={ Text("$it:00") }, onClick={ onPick(it); open=false })
            }
        }
    }
}
