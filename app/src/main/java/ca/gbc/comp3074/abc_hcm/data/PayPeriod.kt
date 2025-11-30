package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PayPeriod(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startDate: String,  // Format: yyyy-MM-dd
    val endDate: String,    // Format: yyyy-MM-dd
    val periodType: String, // "WEEKLY" or "MONTHLY"
    val isProcessed: Boolean = false  // Whether payroll has been calculated for this period
)
