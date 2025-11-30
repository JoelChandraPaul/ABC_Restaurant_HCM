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

    @Insert(onConflict = OnConflictStrategy.ABORT)   // <-- prevents overriding IDs
    suspend fun insert(employee: Employee)

    @Query("SELECT * FROM Employee WHERE employeeId = :id AND password = :password LIMIT 1")
    suspend fun login(id: String, password: String): Employee?

    @Query("SELECT * FROM Employee WHERE employeeId = :id LIMIT 1")
    suspend fun getOne(id: String): Employee?

    @Query("SELECT * FROM Employee")
    suspend fun getAllOnce(): List<Employee>

    @Query("DELETE FROM Employee WHERE employeeId = :id")
    suspend fun delete(id: String)

    @Query("UPDATE Employee SET name = :name, password = :pass, hourlyRate = :rate WHERE employeeId = :id")
    suspend fun update(id: String, name: String, pass: String, rate: Double)

}
