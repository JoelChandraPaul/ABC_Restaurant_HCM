package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import ca.gbc.comp3074.abc_hcm.data.Request
import kotlinx.coroutines.launch

class RequestViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getDatabase(app)
    val requests = db.requestDao().getAll().asLiveData()

    fun addRequest(r: Request) = viewModelScope.launch {
        db.requestDao().insert(r)
    }

    fun updateStatus(id: Int, status: String) = viewModelScope.launch {
        db.requestDao().updateStatus(id, status)
    }
}
