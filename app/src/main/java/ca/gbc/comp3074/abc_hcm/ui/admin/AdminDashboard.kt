package ca.gbc.comp3074.abc_hcm.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AdminDashboard(nav: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Manager Dashboard", fontSize = 26.sp, fontWeight = FontWeight.Bold)

        Button(onClick = { nav.navigate("admin_add_employee") }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Employee")
        }

        Button(onClick = { nav.navigate("admin_employees") }, modifier = Modifier.fillMaxWidth()) {
            Text("View Employees")
        }

        Button(onClick = { nav.navigate("admin_schedule") }, modifier = Modifier.fillMaxWidth()) {
            Text("View Schedule")
        }

        Button(onClick = { nav.navigate("admin_requests") }, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Requests")
        }

        Button(onClick = { nav.navigate("admin_payroll") }, modifier = Modifier.fillMaxWidth()) {  // 🔥 Newly Added
            Text("View Payroll")
        }

        Button(
            onClick = {
                nav.navigate("role_select") {
                    popUpTo("role_select") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Logout") }
    }
}
