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

    suspend fun addEmployee(id: String, name: String, pass: String, rate: Double): Boolean {
        val exists = db.employeeDao().getOne(id)
        return if (exists != null) false else {
            db.employeeDao().insert(Employee(id, name, pass, rate))
            true
        }
    }

    fun deleteEmployee(id: String) = viewModelScope.launch {
        db.employeeDao().delete(id)
    }

    fun updateEmployee(id: String, name: String, pass: String, rate: Double) = viewModelScope.launch {
        db.employeeDao().update(id, name, pass, rate)
    }
}
