package com.myapp.usecase.check;

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import io.micronaut.serde.annotation.Serdeable
import reactor.core.publisher.Mono

@Client(id = "insurance")
interface InsuranceClient {

  @Get("/accidents/report?vin={vin}")
  suspend fun getReport(@QueryValue vin: String): InsuranceReport?

  @Serdeable
  data class InsuranceReport(val report: Report) {

    fun getClaims(): Int {
      return report.claims;
    }

    @Serdeable
    data class Report(val claims: Int)
  }
}
