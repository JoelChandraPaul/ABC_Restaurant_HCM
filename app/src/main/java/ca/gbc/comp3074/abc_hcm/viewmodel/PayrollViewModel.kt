package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class PayrollRecord(
    val employeeId: String,
    val employeeName: String,
    val hoursWorked: Int,
    val hourlyRate: Double,
    val totalPay: Double
)

class PayrollViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getDatabase(app)

    private val _payroll = MutableLiveData<List<PayrollRecord>>()
    val payroll: LiveData<List<PayrollRecord>> get() = _payroll

    init {
        calculatePayroll()  // Initialize payroll calculation
    }

    private fun hoursFromShift(label: String): Int {
        val parts = label.replace(" ", "").split("-")
        if (parts.size != 2) return 0

        val start = parts[0].take(2).toIntOrNull() ?: return 0
        val end = parts[1].take(2).toIntOrNull() ?: return 0

        return if (end >= start) end - start else (24 - start) + end
    }

    // Calculate payroll for all employees
    fun calculatePayroll() = viewModelScope.launch(Dispatchers.IO) {
        val employees = db.employeeDao().getAllOnce()  // Fetch all employees
        val schedules = db.scheduleDao().getAllOnce()  // Fetch all schedules

        val result = employees.map { emp ->
            val mySchedules = schedules.filter { it.employee == emp.employeeId }
            val hours = mySchedules.sumOf { hoursFromShift(it.shift) }

            // Fetch the hourly rate for each employee dynamically
            val hourlyRate = emp.hourlyRate

            PayrollRecord(
                employeeId = emp.employeeId,
                employeeName = emp.name,
                hoursWorked = hours,
                hourlyRate = hourlyRate,  // Use the dynamic hourly rate
                totalPay = hours * hourlyRate  // Calculate total pay
            )
        }

        _payroll.postValue(result)  // Update LiveData with calculated payroll
    }
}
