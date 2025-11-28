package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import ca.gbc.comp3074.abc_hcm.data.Schedule
import kotlinx.coroutines.launch

class ScheduleViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getDatabase(app)
    val schedules = db.scheduleDao().getAll().asLiveData()

    fun addSchedule(s: Schedule) = viewModelScope.launch {
        db.scheduleDao().insert(s)
    }

    fun updateData(id: Int, data: String) = viewModelScope.launch {
        db.scheduleDao().updateData(id, data)
    }
}
