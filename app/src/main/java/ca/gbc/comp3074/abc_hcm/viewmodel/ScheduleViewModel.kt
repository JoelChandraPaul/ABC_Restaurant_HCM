package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import ca.gbc.comp3074.abc_hcm.data.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ScheduleViewModel(app: Application): AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).scheduleDao()

    val schedules: Flow<List<Schedule>> = dao.getAll()

    fun add(day: String, shift: String, employee: String) = viewModelScope.launch {
        dao.insert(Schedule(0, employee, day, shift))  // correct order
    }

    fun delete(id: Int) = viewModelScope.launch {
        dao.delete(id)
    }
}
