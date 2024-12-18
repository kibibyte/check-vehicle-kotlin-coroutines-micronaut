package com.myapp.usecase.check;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.serde.annotation.Serdeable;

@Client(id = "maintenance")
interface MaintenanceClient {

  @Get("/cars/{vin}")
  suspend fun getReport(@PathVariable vin: String): MaintenanceReport?

  @Serdeable
  data class MaintenanceReport(
    @JsonProperty("maintenance_frequency")
    val maintenanceFrequency: MaintenanceFrequency
  )
}
