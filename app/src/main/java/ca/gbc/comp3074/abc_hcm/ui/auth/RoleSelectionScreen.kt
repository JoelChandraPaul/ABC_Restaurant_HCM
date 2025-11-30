package ca.gbc.comp3074.abc_hcm.ui.auth

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
fun RoleSelectionScreen(nav: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("ABC Restaurant HCM", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(28.dp))
        Button(onClick = { nav.navigate("admin_login") }, modifier = Modifier.fillMaxWidth()) {
            Text("Manager Login")
        }
        Spacer(Modifier.height(12.dp))
        Button(onClick = { nav.navigate("employee_login") }, modifier = Modifier.fillMaxWidth()) {
            Text("Employee Login")
        }
    }
}
