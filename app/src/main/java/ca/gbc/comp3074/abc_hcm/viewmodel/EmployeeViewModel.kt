package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import ca.gbc.comp3074.abc_hcm.data.Employee
import kotlinx.coroutines.launch

class EmployeeViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getDatabase(app)
    val employees = db.employeeDao().getAll().asLiveData()

    fun addEmployee(id: String, name: String, password: String) = viewModelScope.launch {
        db.employeeDao().insert(Employee(id, name, password))
    }
}
