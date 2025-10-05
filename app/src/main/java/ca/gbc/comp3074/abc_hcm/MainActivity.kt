package ca.gbc.comp3074.abc_hcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ca.gbc.comp3074.abc_hcm.ui.theme.ABC_HCMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HcmApp() }
    }
}

@Composable
fun HcmApp() {
    MaterialTheme {
        val nav = rememberNavController()
        AppNavGraph(nav)
    }
}

@Composable
fun AppNavGraph(nav: NavHostController) {
    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") { SplashScreen(nav) }
        composable("login") { LoginScreen(nav) }
        composable("manager") { ManagerDashboard(nav) }
        composable("employee") { EmployeeDashboard(nav) }
        composable("schedule") { ScheduleScreen(nav) }
        composable("payroll") { PayrollScreen(nav) }
        composable("requests") { RequestScreen(nav) }
        composable("about") { AboutScreen(nav) }
    }
}

/* -------------------- Screens -------------------- */

@Composable
fun SplashScreen(nav: NavController) {
    LaunchedEffect(Unit) {
        delay(1500)
        nav.navigate("login") { popUpTo("splash") { inclusive = true } }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("ABC Restaurant HCM\nGroup 10", fontSize = 28.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LoginScreen(nav: NavController) {
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login as:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.navigate("manager") }) { Text("Manager") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("employee") }) { Text("Employee") }
    }
}