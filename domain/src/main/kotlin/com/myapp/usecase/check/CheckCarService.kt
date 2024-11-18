package com.myapp.usecase.check;

import com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import com.myapp.usecase.check.CheckCarFeature.MAINTENANCE
import jakarta.inject.Singleton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

@Singleton
class CheckCarService(private val checkCarRepository: CheckCarRepository) {
  companion object {
    private val log = LoggerFactory.getLogger(CheckCarService::class.java)
  }

  suspend fun check(checkCarQuery: CheckCarQuery): CheckCarResult = coroutineScope {
    val vin = checkCarQuery.vin

    val numberOfAccidents: Deferred<Int?> =
      if (checkCarQuery.isCheckFeature(ACCIDENT_FREE))
        async(start = LAZY) { findNumberOfAccidents(vin) }
      else CompletableDeferred(null as? Int)

    val maintenanceFrequency: Deferred<MaintenanceFrequency?> =
      if (checkCarQuery.isCheckFeature(MAINTENANCE))
        async(start = LAZY) { findMaintenanceFrequency(vin) }
      else CompletableDeferred(null as? MaintenanceFrequency)

    CheckCarResult(vin, numberOfAccidents.await(), maintenanceFrequency.await())
  }

  private suspend fun findNumberOfAccidents(vin: String): Int? {
    log.info("Find number of accidents requested")
    return checkCarRepository.findNumberOfAccidents(vin)
  }

  private suspend fun findMaintenanceFrequency(vin: String): MaintenanceFrequency? {
    log.info("Find maintenance frequency requested")
    return checkCarRepository.findMaintenanceFrequency(vin)
  }
}
