package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun register(user:User)

    @Query("SELECT * FROM User WHERE username=:u AND password=:p LIMIT 1")
    suspend fun login(u:String,p:String):User?

    @Query("SELECT * FROM User WHERE role='employee'")
    fun getEmployees():Flow<List<User>>
}
