package com.myapp.usecase.check;

import com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import com.myapp.usecase.check.CheckCarFeature.MAINTENANCE
import jakarta.inject.Singleton
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

    val numberOfAccidents by lazy {
      async {
        if (checkCarQuery.isCheckFeature(ACCIDENT_FREE)) {
          log.info("Find number of accidents requested")
          checkCarRepository.findNumberOfAccidents(vin)
        } else null
      }
    }

    val maintenanceFrequency by lazy {
      async {
        if (checkCarQuery.isCheckFeature(MAINTENANCE)) {
          log.info("Find maintenance frequency requested")
          checkCarRepository.findMaintenanceFrequency(vin)
        } else null
      }
    }

    CheckCarResult(vin, numberOfAccidents.await(), maintenanceFrequency.await())
  }
}
