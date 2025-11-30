package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {

    @Query("SELECT * FROM Request")
    fun getAll(): Flow<List<Request>>

    @Insert
    suspend fun insert(request: Request)

    @Query("UPDATE Request SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)
}
