/*
 * Copyright 2016-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.minifsm.utils;


import com.github.minifsm.Action;
import com.github.minifsm.ActionResult;
import com.github.minifsm.Guard;
import com.github.minifsm.StateContext;
import com.github.minifsm.support.ObjectStateContext;

import java.util.function.Function;
import reactor.core.publisher.Mono;

/**
 * Action Utilities.
 */
public final class ConfigurerUtils {

  private ConfigurerUtils() {
    // This helper class should not be instantiated.
  }

  /**
   * Builds an error calling action {@link Action}.
   *
   * @param <S> the type of state
   * @param <E> the type of event
   * @param action the action
   * @param errorAction the error action
   * @return the error calling action
   */
  public static <S, E> Action<S, E> errorCallingAction(final Action<S, E> action,
      final Action<S, E> errorAction) {
    return context -> {
      try {
        return action.execute(context);
      } catch (Throwable throwable) {
        // notify something wrong is happening in actions execution.
        try {
          errorAction.execute(
              new ObjectStateContext<>(context.getMessage(), context.getStateMachine(), throwable));
        } catch (Exception e) {
          e.printStackTrace();
        }
        throw new RuntimeException(throwable);
      }
    };
  }

  /**
   * Builds a {@link Function} from an {@link Action}.
   *
   * @param <S> the type of state
   * @param <E> the type of event
   * @param action the action
   * @return the function
   */
  public static <S, E> Function<StateContext<S, E>, Mono<ActionResult>> fromAction(
      Action<S, E> action) {
    if (action != null) {
      return context -> Mono.fromCallable(() -> action.execute(context));
    } else {
      return null;
    }
  }

  public static <S, E> Function<StateContext<S, E>, Mono<Boolean>> fromGuard(Guard<S, E> guard) {
    if (guard != null) {
      return context -> Mono.fromSupplier(() -> guard.evaluate(context));
    } else {
      return null;
    }
  }

}
