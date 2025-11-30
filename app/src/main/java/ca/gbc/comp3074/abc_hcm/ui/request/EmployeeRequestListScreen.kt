package ca.gbc.comp3074.abc_hcm.ui.request

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.RequestViewModel

@Composable
fun EmployeeRequestListScreen(nav: NavHostController, employeeId: String, vm: RequestViewModel = viewModel()) {

    val requests = vm.requests.observeAsState(emptyList()).value.filter { it.employee == employeeId }

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Text("My Requests", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(requests) { r ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Type: ${r.type}")
                        Text("Date: ${r.date}")
                        Text("Reason: ${r.reason}")

                        Text(
                            "Status: ${r.status}",
                            color =
                                if (r.status == "Approved") MaterialTheme.colorScheme.primary
                                else if (r.status == "Rejected") MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
