package ca.gbc.comp3074.abc_hcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

/* ------------ App + Nav ------------ */

@Composable
fun App() {
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

        // Manager side
        composable("manager") { ManagerDashboard(nav) }
        composable("manager/schedule") { ManageScheduleScreen(nav) }
        composable("manager/payroll") { PayrollSummaryScreen(nav) }
        composable("manager/requests") { ViewRequestsScreen(nav) }

        // Employee side
        composable("employee") { EmployeeDashboard(nav) }
        composable("employee/mySchedule") { MyScheduleScreen(nav) }
        composable("employee/requestForm") { RequestFormScreen(nav) }
        composable("employee/paySummary") { EmployeePaySummaryScreen(nav) }

        // Common
        composable("about") { AboutScreen(nav) }
    }
}

/* ------------ Screens ------------ */

// Splash
@Composable
fun SplashScreen(nav: NavController) {
    LaunchedEffect(Unit) {
        delay(1500)
        nav.navigate("login") { popUpTo("splash") { inclusive = true } }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("ABC Restaurant HCM\nGroup G-10", fontSize = 28.sp, fontWeight = FontWeight.Bold)
    }
}

// Login (role select)
@Composable
fun LoginScreen(nav: NavController) {
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login as:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.navigate("manager") }, modifier = Modifier.fillMaxWidth()) { Text("Manager") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("employee") }, modifier = Modifier.fillMaxWidth()) { Text("Employee") }
    }
}

/* ------------ Manager ------------ */

@Composable
fun ManagerDashboard(nav: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Manager Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.navigate("manager/schedule") }, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Schedule")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("manager/payroll") }, modifier = Modifier.fillMaxWidth()) {
            Text("Payroll Summary")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("manager/requests") }, modifier = Modifier.fillMaxWidth()) {
            Text("View Requests")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("about") }, modifier = Modifier.fillMaxWidth()) {
            Text("About")
        }
    }
}

data class Shift(val day: String, val employee: String, val time: String)

@Composable
fun ManageScheduleScreen(nav: NavController) {
    val week = listOf(
        Shift("Mon", "John", "9–5"),
        Shift("Tue", "Amy", "11–7"),
        Shift("Wed", "Charles", "9–5"),
        Shift("Thu", "Jingyu", "10–6"),
        Shift("Fri", "Joel", "8–4")
    )
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Weekly Schedule (All Employees)", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        week.forEach {
            Text("${it.day}: ${it.employee}  ${it.time}", fontSize = 18.sp)
            Divider()
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
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
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Payroll Summary", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        payroll.forEach { e ->
            val total = e.hours * e.rate
            Column(Modifier.padding(vertical = 8.dp)) {
                Text(e.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Hours: ${e.hours}")
                Text("Rate: $${"%.2f".format(e.rate)}")
                Text("Total Pay: $${"%.2f".format(total)}")
            }
            Divider()
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

data class RequestItem(val employee: String, val type: String, val date: String, val reason: String)

@Composable
fun ViewRequestsScreen(nav: NavController) {
    val requests = listOf(
        RequestItem("John", "Time Off", "Nov 5, 2025", "Family event"),
        RequestItem("Amy", "Shift Change", "Nov 8, 2025", "Doctor appointment"),
        RequestItem("Charles", "Reimbursement", "Nov 10, 2025", "Kitchen supplies")
    )
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Employee Requests", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        requests.forEach { r ->
            Column(Modifier.padding(vertical = 6.dp)) {
                Text("${r.employee}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Type: ${r.type}")
                Text("Date: ${r.date}")
                Text("Reason: ${r.reason}")
            }
            Divider()
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

/* ------------ Employee ------------ */

@Composable
fun EmployeeDashboard(nav: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Employee Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.navigate("employee/mySchedule") }, modifier = Modifier.fillMaxWidth()) {
            Text("My Schedule")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("employee/requestForm") }, modifier = Modifier.fillMaxWidth()) {
            Text("Request (Time Off / Shift / Reimbursement)")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("employee/paySummary") }, modifier = Modifier.fillMaxWidth()) {
            Text("My Pay Summary")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { nav.navigate("about") }, modifier = Modifier.fillMaxWidth()) {
            Text("About")
        }
    }
}

@Composable
fun MyScheduleScreen(nav: NavController) {
    // Simple “calendar-like” week list (dummy)
    val myWeek = listOf(
        Shift("Mon", "Me", "9–5"),
        Shift("Tue", "Me", "OFF"),
        Shift("Wed", "Me", "9–5"),
        Shift("Thu", "Me", "11–7"),
        Shift("Fri", "Me", "10–6")
    )
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Schedule", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        myWeek.forEach {
            Text("${it.day}: ${it.time}", fontSize = 18.sp)
            Divider()
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

@Composable
fun RequestFormScreen(nav: NavController) {
    var type by remember { mutableStateOf("") }      // Time Off / Shift Change / Reimbursement
    var date by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Submit Request", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = reason, onValueChange = { reason = it },
            label = { Text("Reason / Notes") }, modifier = Modifier.fillMaxWidth(), maxLines = 3
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Submit (dummy)") }
    }
}

@Composable
fun EmployeePaySummaryScreen(nav: NavController) {
    val hours = 38
    val rate = 19.50
    val total = hours * rate
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Pay Summary", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Hours: $hours")
        Text("Hourly Rate: $${"%.2f".format(rate)}")
        Text("Estimated Pay: $${"%.2f".format(total)}")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

/* ------------ About ------------ */

@Composable
fun AboutScreen(nav: NavController) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("ABC Restaurant HCM", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Course: COMP 3074 – Mobile App Development I")
        Text("Group: G-10")
        Spacer(Modifier.height(8.dp))
        Text("Members:")
        Text("• Yueyang Peng")
        Text("• Joel Chandra Paul")
        Text("• Jingyu He")
        Text("• Charles Langmuir")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }) { Text("Back") }
    }
}
