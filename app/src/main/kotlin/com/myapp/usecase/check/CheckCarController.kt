package com.myapp.usecase.check;

import com.myapp.filters.MDCFilter
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import jakarta.validation.Valid
import org.slf4j.MDC

@Controller
open class CheckCarController(private var checkCarService: CheckCarService) {

  @Post(value = "/check", consumes = [APPLICATION_JSON], produces = [APPLICATION_JSON])
  open suspend fun check(@Valid @Body request: CheckCarRequest): CheckCarResponse {
    val checkCarQuery = CheckCarQuery(request.vin, request.features);

    return checkCarService.check(checkCarQuery)
      .let(this::toCheckCarResponse);
  }

  fun toCheckCarResponse(result: CheckCarResult): CheckCarResponse {
    return CheckCarResponse(
      requestId = MDC.get(MDCFilter.REQUEST_ID),
      vin = result.vin,
      accidentFree = result.accidentFree,
      maintenanceScore = result.maintenanceScore
    )
  }
}