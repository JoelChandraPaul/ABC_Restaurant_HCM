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
fun ManagerDashboard(nav: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Manager Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { nav.navigate("manager/schedule") }, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Schedule")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { nav.navigate("manager/payroll") }, modifier = Modifier.fillMaxWidth()) {
            Text("Payroll Summary")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { nav.navigate("manager/requests") }, modifier = Modifier.fillMaxWidth()) {
            Text("View Requests")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { nav.navigate("about") }, modifier = Modifier.fillMaxWidth()) {
            Text("About")
        }
    }
}

data class PayrollEntry(val name: String, val hours: Int, val rate: Double)

@Composable
fun PayrollSummaryScreen(nav: NavController) {
    val payroll = listOf(
        PayrollEntry("John", 40, 20.0),
        PayrollEntry("Amy", 35, 18.5),
        PayrollEntry("Charles", 45, 22.0),
        PayrollEntry("Jingyu", 30, 19.0)
    )
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Payroll Summary", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        payroll.forEach { e ->
            val total = e.hours * e.rate
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(e.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Hours: ${e.hours}")
                Text("Rate: $${"%.2f".format(e.rate)}")
                Text("Total Pay: $${"%.2f".format(total)}")
            }
            Divider()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
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
