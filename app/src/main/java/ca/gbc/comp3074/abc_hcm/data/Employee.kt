package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Employee(
    @PrimaryKey val employeeId: String,
    val name: String,
    val password: String,
    val hourlyRate: Double = 15.0
)
