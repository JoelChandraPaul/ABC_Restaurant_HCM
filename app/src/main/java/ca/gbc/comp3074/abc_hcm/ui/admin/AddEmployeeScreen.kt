package ca.gbc.comp3074.abc_hcm.ui.admin

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.EmployeeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    nav: NavHostController,
    vm: EmployeeViewModel = viewModel()
) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }

    var idError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Register Employee", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Employee ID
            OutlinedTextField(
                value = id,
                onValueChange = {
                    id = it
                    idError = ""
                },
                label = { Text("Employee ID") },
                isError = idError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (idError.isNotEmpty()) Text(idError, color = MaterialTheme.colorScheme.error)

            // Full Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = { Text("Password") },
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError.isNotEmpty()) Text(passwordError, color = MaterialTheme.colorScheme.error)

            // Hourly Rate
            OutlinedTextField(
                value = rate,
                onValueChange = { rate = it },
                label = { Text("Hourly Rate ($/hr)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = {
                    when {
                        id.isBlank() -> idError = "Employee ID is required"
                        password.isBlank() -> passwordError = "Password cannot be empty"
                        rate.isBlank() || rate.toDoubleOrNull() == null -> {
                            Toast.makeText(context, "Enter a valid hourly rate", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            scope.launch {
                                val saved = vm.addEmployee(id, name, password, rate.toDouble())

                                if (!saved) {
                                    idError = "Employee ID already exists"
                                    Toast.makeText(context, "Duplicate ID — try a new one", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Employee Added Successfully!", Toast.LENGTH_SHORT).show()
                                    nav.popBackStack()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) {
                Text("Save Employee", color = MaterialTheme.colorScheme.onPrimary, fontSize = 17.sp)
            }

            OutlinedButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) {
                Text("Back")
            }
        }
    }
}
