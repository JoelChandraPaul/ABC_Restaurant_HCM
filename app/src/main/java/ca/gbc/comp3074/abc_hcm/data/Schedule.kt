package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employee: String,
    val day: String,
    val shift: String
)
