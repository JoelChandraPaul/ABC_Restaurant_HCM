package ca.gbc.comp3074.abc_hcm.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ca.gbc.comp3074.abc_hcm.viewmodel.AuthViewModel

@Composable
fun AdminLoginScreen(nav: NavHostController, vm: AuthViewModel = viewModel()) {

    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val ctx = LocalContext.current
    val focus = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Manager Login", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(22.dp))

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("Manager ID") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focus.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(26.dp))

        Button(
            onClick = {
                vm.loginAdmin(id, password) { ok ->
                    if (ok) nav.navigate("admin_dashboard")
                    else Toast.makeText(ctx, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Login", fontSize = 18.sp) }
    }
}
