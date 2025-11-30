package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import ca.gbc.comp3074.abc_hcm.data.Employee
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getDatabase(app)

    private val adminId = "admin"
    private val adminPassword = "admin123"

    fun loginAdmin(id: String, password: String, onResult: (Boolean) -> Unit) {
        onResult(id == adminId && password == adminPassword)
    }

    fun loginEmployee(id: String, password: String, onResult: (Employee?) -> Unit) {
        viewModelScope.launch {
            val emp = db.employeeDao().login(id, password)
            onResult(emp)
        }
    }
}
