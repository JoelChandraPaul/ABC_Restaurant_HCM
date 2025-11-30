package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import ca.gbc.comp3074.abc_hcm.data.Schedule
import kotlinx.coroutines.launch
import java.time.LocalDate

class ScheduleViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).scheduleDao()
    private val payrollViewModel = PayrollViewModel(app)  // Reference PayrollViewModel

    val schedules = dao.getAll()

    // Add shift
    fun add(date: LocalDate, shift: String, employee: String) = viewModelScope.launch {
        dao.insert(Schedule(0, employee, date.toString(), shift))
        payrollViewModel.calculatePayroll()  // Recalculate payroll after adding a shift
    }

    // Update shift
    fun update(id: Int, date: String, shift: String, employee: String) = viewModelScope.launch {
        dao.update(id, date, shift, employee)
        payrollViewModel.calculatePayroll()  // Recalculate payroll after updating a shift
    }

    // Delete shift
    fun delete(id: Int) = viewModelScope.launch {
        dao.delete(id)
        payrollViewModel.calculatePayroll()  // Recalculate payroll after deleting a shift
    }

    // Get schedules for a specific date
    fun getByDate(date: LocalDate) = dao.getByDate(date.toString())
}
