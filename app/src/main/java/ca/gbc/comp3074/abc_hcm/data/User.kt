package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val username:String,
    val password:String,
    val role:String
)
