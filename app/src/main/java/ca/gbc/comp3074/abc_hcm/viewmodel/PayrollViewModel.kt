package ca.gbc.comp3074.abc_hcm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.abc_hcm.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class PayrollRecord(
    val employeeId: String,
    val employeeName: String,
    val regularHours: Double,
    val overtimeHours: Double,
    val totalHours: Double,
    val hourlyRate: Double,
    val regularPay: Double,
    val overtimePay: Double,
    val grossPay: Double,
    val taxDeduction: Double,
    val insuranceDeduction: Double,
    val otherDeductions: Double,
    val totalDeductions: Double,
    val netPay: Double,
    val payPeriodId: Int? = null,
    val periodStart: String? = null,
    val periodEnd: String? = null
)

class PayrollViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getDatabase(app)

    private val _payroll = MutableLiveData<List<PayrollRecord>>()
    val payroll: LiveData<List<PayrollRecord>> get() = _payroll

    private val _payPeriods = MutableLiveData<List<ca.gbc.comp3074.abc_hcm.data.PayPeriod>>()
    val payPeriods: LiveData<List<ca.gbc.comp3074.abc_hcm.data.PayPeriod>> get() = _payPeriods

    private val _selectedPayPeriod = MutableLiveData<ca.gbc.comp3074.abc_hcm.data.PayPeriod?>()
    val selectedPayPeriod: LiveData<ca.gbc.comp3074.abc_hcm.data.PayPeriod?> get() = _selectedPayPeriod

    private val _payrollHistory = MutableLiveData<List<ca.gbc.comp3074.abc_hcm.data.PayrollHistory>>()
    val payrollHistory: LiveData<List<ca.gbc.comp3074.abc_hcm.data.PayrollHistory>> get() = _payrollHistory

    // Tax and deduction rates (can be made configurable later)
    private val TAX_RATE = 0.15  // 15% tax
    private val INSURANCE_RATE = 0.05  // 5% insurance
    private val OVERTIME_MULTIPLIER = 1.5
    private val REGULAR_HOURS_THRESHOLD = 40.0

    init {
        loadPayPeriods()
        calculatePayroll()  // Initialize payroll calculation with current period
    }

    private fun hoursFromShift(label: String): Double {
        android.util.Log.d("PayrollViewModel", "  Parsing shift: '$label'")

        // Split by '-' and remove spaces: "9:00 - 19:00" → ["9:00", "19:00"]
        val parts = label.split("-").map { it.trim() }
        android.util.Log.d("PayrollViewModel", "  Split parts: $parts (size: ${parts.size})")

        if (parts.size != 2) {
            android.util.Log.d("PayrollViewModel", "  ERROR: Parts size != 2, returning 0.0")
            return 0.0
        }

        // Extract the hour number before the colon: "9:00" → 9, "19:00" → 19
        val start = parts[0].substringBefore(":").toIntOrNull()
        val end = parts[1].substringBefore(":").toIntOrNull()
        android.util.Log.d("PayrollViewModel", "  Start: $start, End: $end")

        if (start == null || end == null) {
            android.util.Log.d("PayrollViewModel", "  ERROR: Could not parse start or end, returning 0.0")
            return 0.0
        }

        val hours = if (end >= start) end - start else (24 - start) + end
        android.util.Log.d("PayrollViewModel", "  Calculated hours: $hours")
        return hours.toDouble()
    }

    // Load all pay periods
    fun loadPayPeriods() = viewModelScope.launch(Dispatchers.IO) {
        val periods = db.payPeriodDao().getAllOnce()
        _payPeriods.postValue(periods)
    }

    // Select a pay period
    fun selectPayPeriod(payPeriod: ca.gbc.comp3074.abc_hcm.data.PayPeriod?) {
        _selectedPayPeriod.value = payPeriod
        calculatePayroll(payPeriod)
    }

    // Calculate payroll for all employees for a specific pay period (or all time if null)
    fun calculatePayroll(payPeriod: ca.gbc.comp3074.abc_hcm.data.PayPeriod? = null) = viewModelScope.launch(Dispatchers.IO) {
        val employees = db.employeeDao().getAllOnce()
        val allSchedules = db.scheduleDao().getAllOnce()

        android.util.Log.d("PayrollViewModel", "=== Starting Payroll Calculation ===")
        android.util.Log.d("PayrollViewModel", "Total employees: ${employees.size}")
        android.util.Log.d("PayrollViewModel", "Total schedules: ${allSchedules.size}")

        val result = employees.map { emp ->
            // Filter schedules by employee and optionally by pay period
            val mySchedules = allSchedules.filter { schedule ->
                schedule.employee == emp.employeeId &&
                (payPeriod == null || isDateInPeriod(schedule.date, payPeriod.startDate, payPeriod.endDate))
            }

            val totalHours = mySchedules.sumOf { hoursFromShift(it.shift) }

            // Calculate regular and overtime hours
            val regularHours = minOf(totalHours, REGULAR_HOURS_THRESHOLD)
            val overtimeHours = maxOf(0.0, totalHours - REGULAR_HOURS_THRESHOLD)

            // Calculate pay
            val hourlyRate = emp.hourlyRate
            val regularPay = regularHours * hourlyRate
            val overtimePay = overtimeHours * hourlyRate * OVERTIME_MULTIPLIER
            val grossPay = regularPay + overtimePay

            // Calculate deductions
            val taxDeduction = grossPay * TAX_RATE
            val insuranceDeduction = grossPay * INSURANCE_RATE
            val otherDeductions = 0.0
            val totalDeductions = taxDeduction + insuranceDeduction + otherDeductions
            val netPay = grossPay - totalDeductions

            android.util.Log.d("PayrollViewModel", "--- Employee: ${emp.name} (${emp.employeeId}) ---")
            android.util.Log.d("PayrollViewModel", "  Schedules: ${mySchedules.size}")
            android.util.Log.d("PayrollViewModel", "  Total Hours: $totalHours")
            android.util.Log.d("PayrollViewModel", "  Regular Hours: $regularHours")
            android.util.Log.d("PayrollViewModel", "  Overtime Hours: $overtimeHours")
            android.util.Log.d("PayrollViewModel", "  Hourly Rate: $hourlyRate")
            android.util.Log.d("PayrollViewModel", "  Regular Pay: $regularPay")
            android.util.Log.d("PayrollViewModel", "  Overtime Pay: $overtimePay")
            android.util.Log.d("PayrollViewModel", "  Gross Pay: $grossPay")
            android.util.Log.d("PayrollViewModel", "  Tax Deduction: $taxDeduction")
            android.util.Log.d("PayrollViewModel", "  Insurance Deduction: $insuranceDeduction")
            android.util.Log.d("PayrollViewModel", "  Net Pay: $netPay")

            PayrollRecord(
                employeeId = emp.employeeId,
                employeeName = emp.name,
                regularHours = regularHours,
                overtimeHours = overtimeHours,
                totalHours = totalHours,
                hourlyRate = hourlyRate,
                regularPay = regularPay,
                overtimePay = overtimePay,
                grossPay = grossPay,
                taxDeduction = taxDeduction,
                insuranceDeduction = insuranceDeduction,
                otherDeductions = otherDeductions,
                totalDeductions = totalDeductions,
                netPay = netPay,
                payPeriodId = payPeriod?.id,
                periodStart = payPeriod?.startDate,
                periodEnd = payPeriod?.endDate
            )
        }

        _payroll.postValue(result)
        android.util.Log.d("PayrollViewModel", "=== Payroll Calculation Complete ===")
    }

    // Save payroll to history
    fun savePayrollToHistory(payPeriod: ca.gbc.comp3074.abc_hcm.data.PayPeriod) = viewModelScope.launch(Dispatchers.IO) {
        android.util.Log.d("PayrollViewModel", "=== Saving Payroll to History ===")
        android.util.Log.d("PayrollViewModel", "Pay Period ID: ${payPeriod.id}, ${payPeriod.startDate} to ${payPeriod.endDate}")

        val currentPayroll = _payroll.value
        if (currentPayroll == null) {
            android.util.Log.d("PayrollViewModel", "ERROR: Current payroll is null!")
            return@launch
        }

        android.util.Log.d("PayrollViewModel", "Current payroll has ${currentPayroll.size} employees")

        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date())

        val historyRecords = currentPayroll.map { record ->
            android.util.Log.d("PayrollViewModel", "  Saving: ${record.employeeName} (${record.employeeId}), NetPay: ${record.netPay}")
            ca.gbc.comp3074.abc_hcm.data.PayrollHistory(
                employeeId = record.employeeId,
                payPeriodId = payPeriod.id,
                regularHours = record.regularHours,
                overtimeHours = record.overtimeHours,
                hourlyRate = record.hourlyRate,
                regularPay = record.regularPay,
                overtimePay = record.overtimePay,
                grossPay = record.grossPay,
                taxDeduction = record.taxDeduction,
                insuranceDeduction = record.insuranceDeduction,
                otherDeductions = record.otherDeductions,
                totalDeductions = record.totalDeductions,
                netPay = record.netPay,
                calculatedDate = currentDate
            )
        }

        android.util.Log.d("PayrollViewModel", "Inserting ${historyRecords.size} history records to database")
        db.payrollHistoryDao().insertAll(historyRecords)
        db.payPeriodDao().markAsProcessed(payPeriod.id)
        android.util.Log.d("PayrollViewModel", "=== Payroll History Saved Successfully ===")
    }

    // Load payroll history for a specific employee
    fun loadPayrollHistory(employeeId: String) = viewModelScope.launch(Dispatchers.IO) {
        android.util.Log.d("PayrollViewModel", "Loading payroll history for employee: $employeeId")
        val history = db.payrollHistoryDao().getByEmployeeOnce(employeeId)
        android.util.Log.d("PayrollViewModel", "Found ${history.size} history records")
        history.forEach { record ->
            android.util.Log.d("PayrollViewModel", "  History: Date=${record.calculatedDate}, NetPay=${record.netPay}")
        }
        _payrollHistory.postValue(history)
    }

    // Create a new pay period
    fun createPayPeriod(startDate: String, endDate: String, periodType: String) = viewModelScope.launch(Dispatchers.IO) {
        val payPeriod = ca.gbc.comp3074.abc_hcm.data.PayPeriod(
            startDate = startDate,
            endDate = endDate,
            periodType = periodType,
            isProcessed = false
        )
        db.payPeriodDao().insert(payPeriod)
        loadPayPeriods()
    }

    // Helper function to check if a date is within a period
    private fun isDateInPeriod(date: String, startDate: String, endDate: String): Boolean {
        return date >= startDate && date <= endDate
    }
}
