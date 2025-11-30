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
fun AdminRequestScreen(nav: NavHostController, vm: RequestViewModel = viewModel()) {

    val requests = vm.requests.observeAsState(emptyList()).value

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Text("Leave / Request Management", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(18.dp))

        LazyColumn {
            items(requests) { r ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(16.dp)) {

                        Text("Employee: ${r.employee}")
                        Text("Type: ${r.type}")
                        Text("Date: ${r.date}")
                        Text("Reason: ${r.reason}")
                        Text("Status: ${r.status}")

                        Spacer(Modifier.height(8.dp))

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = { vm.updateStatus(r.id, "Approved") }) { Text("Approve") }
                            Button(
                                onClick = { vm.updateStatus(r.id, "Rejected") },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                            ) { Text("Reject") }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
