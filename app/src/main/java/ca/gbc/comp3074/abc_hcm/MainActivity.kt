package ca.gbc.comp3074.abc_hcm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.gbc.comp3074.abc_hcm.data.Request
import ca.gbc.comp3074.abc_hcm.viewmodel.RequestViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import ca.gbc.comp3074.abc_hcm.ui.theme.HCMTheme


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

/* ------------ App + Nav ------------ */

@Composable
fun App() {
    HCMTheme {
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
        composable("employee/myRequests") { MyRequestsScreen(nav) }

        // Common
        composable("about") { AboutScreen(nav) }
    }
}

/* ------------ Splash ------------ */

@Composable
fun SplashScreen(nav: NavController) {
    var alpha by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        alpha = 1f
        delay(1500)
        nav.navigate("login") { popUpTo("splash") { inclusive = true } }
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            "ABC Restaurant HCM",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(alpha),
        )
    }
}


/* ------------ Login ------------ */

@Composable
fun LoginScreen(nav: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Login as:", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                // Manager
                Button(
                    onClick = { nav.navigate("manager") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Manager",
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Manager")
                }

                // Employee
                Button(
                    onClick = { nav.navigate("employee") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Employee",
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Employee")
                }
            }
        }
    }
}


/* ------------ Manager ------------ */

@Composable
fun ManagerDashboard(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Manager Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        DashboardButton(
            title = "Manage Schedule",
            icon = Icons.Default.DateRange
        ) { nav.navigate("manager/schedule") }

        DashboardButton(
            title = "Payroll Summary",
            icon = Icons.AutoMirrored.Filled.List
        ) { nav.navigate("manager/payroll") }

        DashboardButton(
            title = "View Requests",
            icon = Icons.AutoMirrored.Filled.List
        ) { nav.navigate("manager/requests") }

        DashboardButton(
            title = "About",
            icon = Icons.Default.Info
        ) { nav.navigate("about") }
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
            HorizontalDivider()
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
            HorizontalDivider()
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}

/* ------------ ViewRequests (Improved) ------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewRequestsScreen(nav: NavController) {
    val viewModel: RequestViewModel = viewModel()
    val requests by viewModel.requests.observeAsState(emptyList())
    val context = LocalContext.current

    Scaffold(topBar = { TopAppBar(title = { Text("Employee Requests") }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (requests.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No requests available", fontSize = 18.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(requests) { r ->
                        val statusColor = when (r.status) {
                            "Approved" -> MaterialTheme.colorScheme.primary
                            "Rejected" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.tertiary
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(6.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = r.employee,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                Spacer(Modifier.height(4.dp))
                                Text("Type: ${r.type}")
                                Text("Date: ${r.date}")
                                Text("Reason: ${r.reason}")
                                Spacer(Modifier.height(6.dp))
                                Surface(
                                    color = statusColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Status: ${r.status}",
                                        color = statusColor,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(Modifier.height(12.dp))
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.updateStatus(r.id, "Approved")
                                            Toast.makeText(context, "Approved!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Approve")
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Button(
                                        onClick = {
                                            viewModel.updateStatus(r.id, "Rejected")
                                            Toast.makeText(context, "Rejected!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Reject")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Back") }
        }
    }
}


/* ------------ Employee ------------ */

@Composable
fun EmployeeDashboard(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Employee Dashboard",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        DashboardButton(
            title = "My Schedule",
            icon = Icons.Default.DateRange
        ) { nav.navigate("employee/mySchedule") }

        DashboardButton(
            title = "Submit Request",
            icon = Icons.Default.Edit
        ) { nav.navigate("employee/requestForm") }

        DashboardButton(
            title = "My Requests",
            icon = Icons.AutoMirrored.Filled.List
        ) { nav.navigate("employee/myRequests") }

        DashboardButton(
            title = "My Pay Summary",
            icon = Icons.AutoMirrored.Filled.List
        ) { nav.navigate("employee/paySummary") }

        DashboardButton(
            title = "About",
            icon = Icons.Default.Info
        ) { nav.navigate("about") }
    }
}


/* ------------ My Requests (New) ------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRequestsScreen(nav: NavController) {
    val viewModel: RequestViewModel = viewModel()
    val requests by viewModel.requests.observeAsState(emptyList())
    val myRequests = requests.filter { it.employee == "Joel" }

    Scaffold(topBar = { TopAppBar(title = { Text("My Requests") }) }) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).fillMaxSize()
        ) {
            if (myRequests.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("You haven’t submitted any requests yet", fontSize = 18.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(myRequests) { r ->
                        val bgColor = when (r.status) {
                            "Approved" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            "Rejected" -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = bgColor),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Type: ${r.type}", fontWeight = FontWeight.Bold)
                                Text("Date: ${r.date}")
                                Text("Reason: ${r.reason}")
                                Text("Status: ${r.status}", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) { Text("Back") }
        }
    }
}

@Composable
fun MyScheduleScreen(nav: NavController) {
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
            HorizontalDivider()
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = { nav.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}


/* ------------ RequestForm (Room Connected) ------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestFormScreen(nav: NavController) {
    val viewModel: RequestViewModel = viewModel()
    var type by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    val context = LocalContext.current

    val calendar = java.util.Calendar.getInstance()
    val year = calendar.get(java.util.Calendar.YEAR)
    val month = calendar.get(java.util.Calendar.MONTH)
    val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        },
        year, month, day
    )

    Scaffold(topBar = { TopAppBar(title = { Text("Submit Request") }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Select Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }
                }
            )

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Reason / Notes") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (type.isNotBlank() && date.isNotBlank()) {
                        val req = Request(employee = "Joel", type = type, date = date, reason = reason)
                        viewModel.addRequest(req)
                        Toast.makeText(context, "Request submitted", Toast.LENGTH_SHORT).show()
                        nav.popBackStack()
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}


/* ------------ Payroll + About ------------ */

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

@Composable
fun DashboardButton(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },  // 让整个卡片可点击
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}



