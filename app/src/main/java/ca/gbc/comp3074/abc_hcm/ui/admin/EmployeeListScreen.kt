package ca.gbc.comp3074.abc_hcm.ui.admin

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
import ca.gbc.comp3074.abc_hcm.viewmodel.EmployeeViewModel

@Composable
fun EmployeeListScreen(nav: NavHostController, vm: EmployeeViewModel = viewModel()) {

    val employees by vm.employees.observeAsState(emptyList())

    Column(Modifier.fillMaxSize().padding(20.dp)) {

        Text("All Employees", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(14.dp))

        LazyColumn {
            items(employees) { emp ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("ID: ${emp.employeeId}")
                        Text("Name: ${emp.name}")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { nav.popBackStack() }
        ) { Text("Back") }
    }
}
