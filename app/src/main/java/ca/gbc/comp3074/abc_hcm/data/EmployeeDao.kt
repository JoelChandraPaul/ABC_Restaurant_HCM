package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM Employee")
    fun getAll(): Flow<List<Employee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee)

    @Query("SELECT * FROM Employee WHERE employeeId = :id AND password = :password LIMIT 1")
    suspend fun login(id: String, password: String): Employee?
}
