package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import kotlinx.coroutines.flow.map

class PayrollViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getDatabase(app)

    val payroll = db.scheduleDao().getAll().map { schedules ->

        schedules.groupBy { it.employee }.map { (empId, shifts) ->

            val employee = db.employeeDao().getOne(empId)
            val hours = shifts.sumOf { it.hours }
            val rate = employee?.hourlyRate ?: 0.0

            PayrollData(
                employeeId = empId,
                employeeName = employee?.name ?: "Unknown",
                hoursWorked = hours,
                hourlyRate = rate,
                totalPay = hours * rate
            )
        }
    }.asLiveData()
}

data class PayrollData(
    val employeeId: String,
    val employeeName: String,
    val hoursWorked: Int,
    val hourlyRate: Double,
    val totalPay: Double
)
