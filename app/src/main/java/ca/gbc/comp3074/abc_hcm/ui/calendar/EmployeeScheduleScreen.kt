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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.gbc.comp3074.abc_hcm.data.Schedule
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScheduleScreen(
    nav: NavController,
    employeeId: String,
    vm: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var sortByHours by remember { mutableStateOf(false) }

    val allSchedules = vm.schedules.collectAsState(initial = emptyList()).value
        .filter { it.employee == employeeId }

    fun getHours(shift: String): Int {
        val parts = shift.split("-").map { it.trim() }
        val s = parts[0].substringBefore(":").toIntOrNull() ?: return 0
        val e = parts.getOrNull(1)?.substringBefore(":")?.toIntOrNull() ?: return 0
        return if (e >= s) e - s else (24 - s) + e
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
        .let { list -> if (sortByHours) list.sortedByDescending { getHours(it.shift) } else list.sortedBy { getDayOrder(it.date) } }

    Scaffold(topBar = { TopAppBar(title = { Text("My Schedule", fontWeight = FontWeight.Bold) }) }) { pad ->

        Column(Modifier.fillMaxSize().padding(pad).padding(20.dp)) {

            if (allSchedules.isEmpty()) EmptyScheduleState()
            else {
                SummarySection(schedules.size, schedules.sumOf { getHours(it.shift) })

                Spacer(Modifier.height(24.dp))
                FiltersSection(selectedFilter) { selectedFilter = it }
                Spacer(Modifier.height(18.dp))
                SortRow(sortByHours) { sortByHours = !sortByHours }
                Spacer(Modifier.height(20.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(22.dp), modifier = Modifier.weight(1f)) {
                    items(schedules) { s ->
                        ScheduleCardLarge(s, getHours(s.shift))
                    }
                }
            }

            Spacer(Modifier.height(25.dp))

            Button(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                Spacer(Modifier.width(6.dp))
                Text("Back to Dashboard", color = Color.White)
            }
        }
    }
}

/* -------------------------------------------------- */
/* COMPONENTS                                         */
/* -------------------------------------------------- */

@Composable
private fun SummarySection(shiftCount: Int, hours: Int) =
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Work Summary", fontWeight = FontWeight.Bold, color = Color.Black.copy(.8f))
            Spacer(Modifier.height(15.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryBox("Shifts", shiftCount.toString())
                SummaryBox("Hours", hours.toString())
            }
        }
    }

@Composable
private fun SummaryBox(label: String, value: String) =
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.headlineMedium.fontSize)
        Spacer(Modifier.height(4.dp))
        Text(label, color = Color.Gray)
    }

@Composable
private fun FiltersSection(selected: String, onSelect: (String) -> Unit) {
    val list = listOf("All", "Morning", "Afternoon", "Evening", "Night")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(list) { f ->
            FilterChip(
                selected = selected == f,
                onClick = { onSelect(f) },
                label = { Text(f) },
                leadingIcon = if (selected == f) ({ Icon(Icons.Default.Check, null) }) else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(.25f),
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun SortRow(sortByHours: Boolean, onToggle: () -> Unit) =
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (sortByHours) "Sorting by Hours" else "Sorting by Day",
            fontWeight = FontWeight.Medium
        )
        IconButton(onClick = onToggle) { Icon(Icons.Default.Refresh, null) }
    }

@Composable
private fun ScheduleCardLarge(s: Schedule, hours: Int) {
    val formattedDay = LocalDate.parse(s.date).dayOfWeek.name

    Card(
        Modifier.fillMaxWidth().height(95.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.fillMaxSize().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {

            Box(
                Modifier.size(58.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.verticalGradient(getShiftColors(hours))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Schedule, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                // Display the formatted day of the week
                Text(formattedDay, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Text(s.shift, color = Color.DarkGray, fontWeight = FontWeight.Medium)
            }

            Surface(shape = RoundedCornerShape(14.dp), color = Color(0xFFDFF6FF)) {
                Text("${hours}h", Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun getShiftColors(h: Int) = when (h) {
    in 6..11 -> listOf(Color(0xFFFFB74D), Color(0xFFFF9800))
    in 12..16 -> listOf(Color(0xFF64B5F6), Color(0xFF2196F3))
    in 17..21 -> listOf(Color(0xFFBA68C8), Color(0xFF9C27B0))
    else -> listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
}

private fun getDayOrder(day: String) = listOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
    .indexOf(day.lowercase()).takeIf { it >= 0 } ?: 99

@Composable private fun EmptyScheduleState() = Text("No shifts scheduled.", fontWeight = FontWeight.Bold)
