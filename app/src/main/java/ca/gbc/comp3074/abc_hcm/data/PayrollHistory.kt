package ca.gbc.comp3074.abc_hcm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PayrollHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: String,
    val payPeriodId: Int,
    val regularHours: Double,        // Regular hours (up to 40/week)
    val overtimeHours: Double,       // Overtime hours (over 40/week)
    val hourlyRate: Double,
    val regularPay: Double,          // regularHours * hourlyRate
    val overtimePay: Double,         // overtimeHours * hourlyRate * 1.5
    val grossPay: Double,            // regularPay + overtimePay
    val taxDeduction: Double,        // Tax deductions
    val insuranceDeduction: Double,  // Insurance deductions
    val otherDeductions: Double,     // Other deductions
    val totalDeductions: Double,     // Sum of all deductions
    val netPay: Double,              // grossPay - totalDeductions
    val calculatedDate: String       // When this payroll was calculated
)
