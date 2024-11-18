package com.myapp.usecase.check

import spock.lang.Specification

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE
import static com.myapp.usecase.check.MaintenanceFrequency.HIGH

class CheckCarServiceTest extends Specification {

  private final CheckCarRepository checkCarRepository = Mock()
  private final CheckCarService checkCarService = new CheckCarService(checkCarRepository)

  def "should check car with success"() {
    when:
    def vin = "1234"
    def checkCarQuery = new CheckCarQuery(vin, featuresToCheck)
    checkCarRepository.findNumberOfAccidents(vin, _) >> 0
    checkCarRepository.findMaintenanceFrequency(vin, _) >> HIGH
    def checkCarResult = SuspendUtils.runBlocking {
      checkCarService.check(checkCarQuery, it)
    }

    then:
    checkCarResult == expectedResult

    where:
    featuresToCheck              || expectedResult
    [ACCIDENT_FREE, MAINTENANCE] || new CheckCarResult("1234", 0, HIGH)
    [ACCIDENT_FREE]              || CheckCarResult.of("1234", 0)
    [MAINTENANCE]                || CheckCarResult.of("1234", HIGH)
  }
}