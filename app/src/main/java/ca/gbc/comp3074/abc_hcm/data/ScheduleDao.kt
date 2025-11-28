package ca.gbc.comp3074.abc_hcm.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM Schedule")
    fun getAll(): Flow<List<Schedule>>

    @Insert
    suspend fun insert(schedule: Schedule)

    @Query("UPDATE Schedule SET data = :data WHERE id = :id")
    suspend fun updateData(id: Int, data: String)
}