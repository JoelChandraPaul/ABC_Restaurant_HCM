package ca.gbc.comp3074.abc_hcm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PayPeriodDao {

    @Query("SELECT * FROM PayPeriod ORDER BY startDate DESC")
    fun getAll(): Flow<List<PayPeriod>>

    @Query("SELECT * FROM PayPeriod WHERE id = :id")
    suspend fun getById(id: Int): PayPeriod?

    @Query("SELECT * FROM PayPeriod ORDER BY startDate DESC")
    suspend fun getAllOnce(): List<PayPeriod>

    @Query("SELECT * FROM PayPeriod WHERE isProcessed = 0 ORDER BY startDate DESC LIMIT 1")
    suspend fun getCurrentUnprocessedPeriod(): PayPeriod?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payPeriod: PayPeriod): Long

    @Update
    suspend fun update(payPeriod: PayPeriod)

    @Query("UPDATE PayPeriod SET isProcessed = 1 WHERE id = :id")
    suspend fun markAsProcessed(id: Int)

    @Delete
    suspend fun delete(payPeriod: PayPeriod)
}
