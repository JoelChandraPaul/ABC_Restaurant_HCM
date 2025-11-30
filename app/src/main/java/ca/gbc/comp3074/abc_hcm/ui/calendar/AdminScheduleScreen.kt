package ca.gbc.comp3074.abc_hcm.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel

@Composable
fun AdminScheduleScreen(nav: NavController, vm: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val schedules = vm.schedules.collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("All Employee Schedules", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(14.dp))

        LazyColumn(Modifier.weight(1f)) {
            items(schedules) { s ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Employee: ${s.employee}")
                        Text("Day: ${s.day}")
                        Text("Shift: ${s.shift}")

                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { vm.delete(s.id) }) {
                            Text("Delete Shift")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { nav.navigate("admin_add_schedule") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Add New Shift") }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Back") }
    }
}
