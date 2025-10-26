package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Request(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employee: String,
    val type: String,
    val date: String,
    val reason: String,
    val status: String = "Pending"
)
