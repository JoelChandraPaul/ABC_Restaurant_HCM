package ca.gbc.comp3074.abc_hcm.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.gbc.comp3074.abc_hcm.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScheduleScreen(
    nav: NavController,
    vm: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val schedules = vm.schedules.collectAsState(initial = emptyList()).value
        .sortedBy { it.day }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Manage Schedules", fontWeight = FontWeight.Bold) })
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.weight(1f)) {
                items(schedules) { s ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Employee: ${s.employee}", fontWeight = FontWeight.SemiBold)
                            Text("Day: ${s.day}")
                            Text("Shift: ${s.shift}")

                            Spacer(Modifier.height(6.dp))

                            Button(
                                onClick = { vm.delete(s.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                modifier = Modifier.fillMaxWidth().height(45.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Delete Shift", color = Color.White)
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { nav.navigate("admin_add_schedule") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) { Text("Add New Shift", color = Color.White) }

            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) { Text("Back") }
        }
    }
}
