package ca.gbc.comp3074.abc_hcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ca.gbc.comp3074.abc_hcm.ui.admin.*
import ca.gbc.comp3074.abc_hcm.ui.auth.*
import ca.gbc.comp3074.abc_hcm.ui.calendar.*
import ca.gbc.comp3074.abc_hcm.ui.employee.*
import ca.gbc.comp3074.abc_hcm.ui.payroll.*
import ca.gbc.comp3074.abc_hcm.ui.request.*
import ca.gbc.comp3074.abc_hcm.ui.theme.HCMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HCMApp() }
    }
}

@Composable
fun HCMApp() {
    HCMTheme {
        Surface(color = MaterialTheme.colorScheme.background) {

            val nav = rememberNavController()

            NavHost(navController = nav, startDestination = "role_select") {

                // LOGIN
                composable("role_select") { RoleSelectionScreen(nav) }
                composable("admin_login") { AdminLoginScreen(nav) }
                composable("employee_login") { EmployeeLoginScreen(nav) }

                // ADMIN
                composable("admin_dashboard") { AdminDashboard(nav) }
                composable("admin_add_employee") { AddEmployeeScreen(nav) }
                composable("admin_employees") { EmployeeListScreen(nav) }
                composable("admin_schedule") { AdminScheduleScreen(nav) }
                composable("admin_add_schedule") { AddScheduleScreen(nav) }
                composable("admin_requests") { AdminRequestScreen(nav) }

                composable("admin_payroll") { AdminPayrollScreen(nav) }

                // EMPLOYEE
                composable("employee_home/{id}") { back ->
                    EmployeeDashboard(nav, back.arguments!!.getString("id")!!)
                }
                composable("employee_schedule/{id}") { back ->
                    EmployeeScheduleScreen(nav, back.arguments!!.getString("id")!!)
                }
                composable("employee_request_form/{id}") { back ->
                    EmployeeRequestScreen(nav, back.arguments!!.getString("id")!!)
                }
                composable("employee_request_list/{id}") { back ->
                    EmployeeRequestListScreen(nav, back.arguments!!.getString("id")!!)
                }

                composable("employee_payroll/{id}") { back ->
                    EmployeePayrollScreen(nav, back.arguments!!.getString("id")!!)
                }
            }
        }
    }
}
