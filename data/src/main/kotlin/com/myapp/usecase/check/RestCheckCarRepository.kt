package com.myapp.usecase.check;

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class RestCheckCarRepository(
  private val insuranceClient: InsuranceClient,
  private val maintenanceClient: MaintenanceClient
) : CheckCarRepository {

  companion object {
    private val log = LoggerFactory.getLogger(RestCheckCarRepository::class.java)
  }

  override suspend fun findNumberOfAccidents(vin: String): Int? {
    return try {
      insuranceClient.getReport(vin).let { it?.getClaims() }
    } catch (e: Exception) {
      if (is404Error(e)) null else throw CheckCarExceptions.restRepositoryException()
    }
  }

  override suspend fun findMaintenanceFrequency(vin: String): MaintenanceFrequency? {
    return try {
      return maintenanceClient.getReport(vin).let { it?.maintenanceFrequency }
    } catch (e: Exception) {
      if (is404Error(e)) null else throw CheckCarExceptions.restRepositoryException()
    }
  }

  private fun is404Error(e: Throwable): Boolean {
    if (e is HttpClientResponseException) {
      return e.status.equals(HttpStatus.NOT_FOUND);
    }
    return false;
  }
}
