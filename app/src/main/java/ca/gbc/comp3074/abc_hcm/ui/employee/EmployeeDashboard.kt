package ca.gbc.comp3074.abc_hcm.ui.employee

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
fun EmployeeDashboard(nav: NavHostController, id: String) {

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Welcome, $id", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Button(onClick = { nav.navigate("employee_schedule/$id") }, modifier = Modifier.fillMaxWidth()) {
            Text("View My Schedule")
        }

        Button(onClick = { nav.navigate("employee_request_form/$id") }, modifier = Modifier.fillMaxWidth()) {
            Text("Submit Leave Request")
        }

        Button(onClick = { nav.navigate("employee_request_list/$id") }, modifier = Modifier.fillMaxWidth()) {
            Text("My Requests")
        }

        Button(onClick = {
            nav.navigate("role_select") {
                popUpTo("role_select") { inclusive = true }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Logout") }
    }
}
