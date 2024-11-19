package com.myapp.usecase.check;

import com.myapp.filters.MDCFilter
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import org.slf4j.MDC

@Controller
class CheckCarController(private var checkCarService: CheckCarService) {

  @Post(value = "/check", consumes = [APPLICATION_JSON], produces = [APPLICATION_JSON])
  suspend fun check(@Body request: CheckCarRequest): CheckCarResponse {
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