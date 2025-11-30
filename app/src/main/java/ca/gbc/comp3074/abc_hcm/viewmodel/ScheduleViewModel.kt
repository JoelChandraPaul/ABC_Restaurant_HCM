package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import ca.gbc.comp3074.abc_hcm.data.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
class ScheduleViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).scheduleDao()
    private val payrollViewModel = PayrollViewModel(app)

    val schedules = dao.getAll()

    fun add(date: LocalDate, shift: String, employee: String) {
        viewModelScope.launch {
            dao.insert(Schedule(0, employee, date.toString(), shift))
            payrollViewModel.calculatePayroll()
        }
    }

    fun update(id: Int, date: String, shift: String, employee: String) {
        viewModelScope.launch {
            dao.update(id, date, shift, employee)
            payrollViewModel.calculatePayroll()
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            dao.delete(id)
            payrollViewModel.calculatePayroll()
        }
    }

    fun getByDate(date: LocalDate): Flow<List<Schedule>> =
        dao.getByDate(date.toString())
}
