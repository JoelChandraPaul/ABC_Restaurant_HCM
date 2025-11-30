package ca.gbc.comp3074.abc_hcm.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.gbc.comp3074.abc_hcm.data.Schedule
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScheduleScreen(
    nav: NavController,
    employeeId: String,
    vm: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var sortByHours by remember { mutableStateOf(false) }
    var selectedSchedule by remember { mutableStateOf<Schedule?>(null) }
    var showRequestDialog by remember { mutableStateOf(false) }

    val allSchedules = vm.schedules.collectAsState(initial = emptyList()).value
        .filter { it.employee == employeeId }

    fun getHours(shift: String): Int {
        val parts = shift.replace(" ", "").split("-")
        val s = parts[0].take(2).toIntOrNull() ?: return 0
        val e = parts.getOrNull(1)?.take(2)?.toIntOrNull() ?: return 0
        return if (e >= s) e - s else 24 - s + e
    }

    fun shiftType(shift: String): String {
        val start = shift.take(2).toIntOrNull() ?: 0
        return when (start) {
            in 6..11 -> "Morning"
            in 12..16 -> "Afternoon"
            in 17..21 -> "Evening"
            else -> "Night"
        }
    }

    val schedules = allSchedules
        .filter { selectedFilter == "All" || shiftType(it.shift) == selectedFilter }
        .let { list -> if (sortByHours) list.sortedByDescending { getHours(it.shift) } else list.sortedBy { getDayOrder(it.day) } }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Schedule", fontWeight = FontWeight.Bold) }) }
    ) { p ->

        Column(Modifier.fillMaxSize().padding(p).padding(20.dp)) {

            if (allSchedules.isEmpty()) EmptyScheduleState()
            else {
                ScheduleSummaryCard(
                    hours = schedules.sumOf { getHours(it.shift) },
                    total = schedules.size
                )

                Spacer(Modifier.height(16.dp))
                FilterChipsRow(selectedFilter) { selectedFilter = it }
                Spacer(Modifier.height(12.dp))
                SortToggle(sortByHours) { sortByHours = !sortByHours }
                Spacer(Modifier.height(16.dp))

                if (schedules.isEmpty()) EmptyFilterState()
                else LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                    items(schedules) { s ->
                        EnhancedScheduleCard(schedule = s, hours = getHours(s.shift)) {
                            selectedSchedule = s
                            showRequestDialog = true
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                Spacer(Modifier.width(8.dp))
                Text("Back to Dashboard")
            }
        }
    }

    if (showRequestDialog && selectedSchedule != null)
        RequestDialog(selectedSchedule!!) { showRequestDialog = false }
}


@Composable
private fun ScheduleSummaryCard(hours: Int, total: Int) = Card(
    Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
) {
    Row(Modifier.padding(20.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        SummaryItem("Total Shifts", total.toString())
        SummaryItem("Total Hours", hours.toString())
    }
}

@Composable
private fun SummaryItem(label: String, value: String) = Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(value, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineSmall.fontSize)
    Text(label, color = Color.Gray)
}

@Composable
private fun FilterChipsRow(selected: String, onSelect: (String) -> Unit) {
    val list = listOf("All", "Morning", "Afternoon", "Evening", "Night")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(list) { f ->
            FilterChip(
                selected = selected == f,
                onClick = { onSelect(f) },
                label = { Text(f) },
                leadingIcon = if (selected == f) { { Icon(Icons.Default.Check, null) } } else null
            )
        }
    }
}

@Composable
private fun SortToggle(sortByHours: Boolean, onToggle: () -> Unit) = Row(
    Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Text(if (sortByHours) "Sorting by Hours" else "Sorting by Day")
    IconButton(onClick = onToggle) { Icon(Icons.Default.Refresh, null) }
}

@Composable
private fun EnhancedScheduleCard(schedule: Schedule, hours: Int, onClick: () -> Unit) =
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Box(
                Modifier.size(55.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.verticalGradient(getShiftColors(hours))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Schedule, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(schedule.day, fontWeight = FontWeight.Bold)
                Text(schedule.shift)
            }

            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFDDF5FF)) {
                Text("$hours h", Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
            }
        }
    }

private fun getShiftColors(h: Int) = when (h) {
    in 6..11 -> listOf(Color(0xFFFFB74D), Color(0xFFFF9800))
    in 12..16 -> listOf(Color(0xFF64B5F6), Color(0xFF2196F3))
    in 17..21 -> listOf(Color(0xFFBA68C8), Color(0xFF9C27B0))
    else -> listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
}

private fun getDayOrder(day: String) = listOf(
    "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
).indexOf(day.lowercase()).takeIf { it >= 0 } ?: 99

@Composable private fun EmptyScheduleState() = Text("No Schedules Yet", modifier = Modifier.fillMaxSize(), fontWeight = FontWeight.Bold)
@Composable private fun EmptyFilterState() = Text("No schedules match this filter")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RequestDialog(s: Schedule, onDismiss: () -> Unit) = AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Request – ${s.day}") },
    text = { Text(s.shift) },
    confirmButton = { Button(onClick = onDismiss) { Text("OK") } }
)
