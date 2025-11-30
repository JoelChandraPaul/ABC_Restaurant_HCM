package ca.gbc.comp3074.abc_hcm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Employee::class,
        Schedule::class,
        Request::class,
        User::class,
        PayPeriod::class,
        PayrollHistory::class
    ],
    version = 11,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun requestDao(): RequestDao
    abstract fun userDao(): UserDao
    abstract fun payPeriodDao(): PayPeriodDao
    abstract fun payrollHistoryDao(): PayrollHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "abc_hcm_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
