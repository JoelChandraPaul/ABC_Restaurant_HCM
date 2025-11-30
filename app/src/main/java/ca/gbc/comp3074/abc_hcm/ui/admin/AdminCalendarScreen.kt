package ca.gbc.comp3074.abc_hcm.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCalendarScreen(
    nav: NavController,
    vm: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Schedule", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { pad ->

        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            MonthHeader(
                month = currentMonth,
                onPrev = { currentMonth = currentMonth.minusMonths(1) },
                onNext = { currentMonth = currentMonth.plusMonths(1) }
            )

            Spacer(Modifier.height(16.dp))

            CalendarGridView(
                month = currentMonth,
                selectedDate = selectedDate,
                onSelect = { selectedDate = it }
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val day = selectedDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
                    nav.navigate("admin_add_schedule?day=$day")
                },
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) { Text("Add Shift for ${selectedDate.dayOfWeek}".replace("_", " ")) }
        }
    }
}

@Composable
private fun MonthHeader(month: YearMonth, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev) { Icon(Icons.Filled.KeyboardArrowLeft, null) }
        Text(
            month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + month.year,
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        IconButton(onClick = onNext) { Icon(Icons.Filled.KeyboardArrowRight, null) }
    }
}

@Composable
private fun CalendarGridView(month: YearMonth, selectedDate: LocalDate, onSelect: (LocalDate) -> Unit) {
    val daysInMonth = month.lengthOfMonth()
    val firstDay = month.atDay(1).dayOfWeek.value % 7

    LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
        items(firstDay) { Box(Modifier.size(42.dp)) }

        items(daysInMonth) { index ->
            val date = month.atDay(index + 1)
            val isSelected = selectedDate == date

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .padding(4.dp)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { onSelect(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (index + 1).toString(),
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}
