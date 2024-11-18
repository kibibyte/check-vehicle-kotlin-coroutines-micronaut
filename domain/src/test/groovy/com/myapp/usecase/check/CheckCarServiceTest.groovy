package com.myapp.usecase.check

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import spock.lang.Specification

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE
import static com.myapp.usecase.check.MaintenanceFrequency.HIGH

class CheckCarServiceTest extends Specification {

  private final CheckCarRepository checkCarRepository = Mock()
  private final CheckCarService checkCarService = new CheckCarService(checkCarRepository)

  def "should check car with success"() {
    def c = GroovyMock(Continuation) {
      getContext() >> GroovyMock(EmptyCoroutineContext)
    }

    when:
    def vin = "1234"
    def checkCarQuery = new CheckCarQuery(vin, [ACCIDENT_FREE, MAINTENANCE])

    //
    CheckCarResult checkCarResult = SuspendUtils.runBlocking {
      checkCarRepository.findNumberOfAccidents(vin, it) >> 0
      checkCarRepository.findMaintenanceFrequency(vin, it) >> HIGH
      checkCarService.check(checkCarQuery, it)
    } as CheckCarResult


    then:
    checkCarResult == new CheckCarResult("1234", 0, HIGH)

  }
}