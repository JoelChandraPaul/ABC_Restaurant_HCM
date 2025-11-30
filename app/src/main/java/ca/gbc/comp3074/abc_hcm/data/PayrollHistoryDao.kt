package ca.gbc.comp3074.abc_hcm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PayrollHistoryDao {

    @Query("SELECT * FROM PayrollHistory ORDER BY calculatedDate DESC")
    fun getAll(): Flow<List<PayrollHistory>>

    @Query("SELECT * FROM PayrollHistory WHERE employeeId = :employeeId ORDER BY calculatedDate DESC")
    fun getByEmployee(employeeId: String): Flow<List<PayrollHistory>>

    @Query("SELECT * FROM PayrollHistory WHERE employeeId = :employeeId ORDER BY calculatedDate DESC")
    suspend fun getByEmployeeOnce(employeeId: String): List<PayrollHistory>

    @Query("SELECT * FROM PayrollHistory WHERE payPeriodId = :payPeriodId")
    suspend fun getByPayPeriod(payPeriodId: Int): List<PayrollHistory>

    @Query("SELECT * FROM PayrollHistory WHERE employeeId = :employeeId AND payPeriodId = :payPeriodId")
    suspend fun getByEmployeeAndPeriod(employeeId: String, payPeriodId: Int): PayrollHistory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payrollHistory: PayrollHistory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payrollHistories: List<PayrollHistory>)

    @Update
    suspend fun update(payrollHistory: PayrollHistory)

    @Delete
    suspend fun delete(payrollHistory: PayrollHistory)

    @Query("DELETE FROM PayrollHistory WHERE payPeriodId = :payPeriodId")
    suspend fun deleteByPayPeriod(payPeriodId: Int)
}
