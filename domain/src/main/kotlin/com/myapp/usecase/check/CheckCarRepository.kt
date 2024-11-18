package com.myapp.usecase.check;

interface CheckCarRepository {

  suspend fun findNumberOfAccidents(vin: String): Int?

  suspend fun findMaintenanceFrequency(vin: String): MaintenanceFrequency?
}
