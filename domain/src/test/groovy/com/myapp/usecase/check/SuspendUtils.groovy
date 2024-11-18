package com.myapp.usecase.check

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.BuildersKt

import java.util.function.Function

class SuspendUtils {
  static <T> T runBlocking(Function<Continuation<? super T>, T> func) {
    try {
      return BuildersKt.runBlocking(
          EmptyCoroutineContext.INSTANCE,
          (s, c) -> func.apply(c)
      );
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
