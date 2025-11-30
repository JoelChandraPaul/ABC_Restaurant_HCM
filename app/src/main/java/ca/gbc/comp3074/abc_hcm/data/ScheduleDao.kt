package ca.gbc.comp3074.abc_hcm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM Schedule")
    fun getAll(): Flow<List<Schedule>>

    @Query("SELECT * FROM Schedule WHERE date = :date")
    fun getByDate(date: String): Flow<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: Schedule)

    @Query("DELETE FROM Schedule WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM Schedule")
    suspend fun getAllOnce(): List<Schedule>

    @Query("UPDATE Schedule SET date = :date, shift = :shift, employee = :employee WHERE id = :id")
    suspend fun update(id: Int, date: String, shift: String, employee: String)
}
