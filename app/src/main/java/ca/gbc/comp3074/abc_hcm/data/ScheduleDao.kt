package ca.gbc.comp3074.abc_hcm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM Schedule")
    fun getAll(): Flow<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: Schedule)

    @Query("DELETE FROM Schedule WHERE id = :id")
    suspend fun delete(id: Int)
}
