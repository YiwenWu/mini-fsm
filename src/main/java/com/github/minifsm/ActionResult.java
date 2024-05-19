package com.github.minifsm;

import java.util.HashMap;
import java.util.Map;

/**
 * Action Result of execute
 */
public interface ActionResult {

  /**
   * get result extra info
   */
  Map<String, Object> infos();

  /**
   * get error info of action apply
   */
  Throwable getThrowable();

  /**
   * get action elapsedTime of millisecond
   *
   * @return action elapsed time
   */
  Long getElapsedTime();

  /**
   * set action elapsedTime of millisecond
   */
  void setElapsedTime(Long elapsedTime);

  static ActionResult of() {
    return new DefaultActionResult(new HashMap<>(), null);
  }

  static ActionResult of(Map<String, Object> infos) {
    return new DefaultActionResult(infos, null);
  }

  static ActionResult of(Throwable throwable) {
    return new DefaultActionResult(new HashMap<>(), throwable);
  }

  class DefaultActionResult implements ActionResult {

    private final Map<String, Object> infos;
    private final Throwable throwable;
    private Long elapsedTime;


    public DefaultActionResult(Map<String, Object> infos, Throwable throwable) {
      this.infos = infos;
      this.throwable = throwable;
    }

    @Override
    public Map<String, Object> infos() {
      return infos;
    }

    @Override
    public Throwable getThrowable() {
      return throwable;
    }

    @Override
    public Long getElapsedTime() {
      return elapsedTime;
    }

    @Override
    public void setElapsedTime(Long elapsedTime) {
      this.elapsedTime = elapsedTime;
    }


    @Override
    public String toString() {
      final StringBuffer sb = new StringBuffer("DefaultActionResult{");
      sb.append("infos=").append(infos);
      sb.append(", throwable=").append(throwable);
      sb.append(", elapsedTime=").append(elapsedTime);
      sb.append('}');
      return sb.toString();
    }
  }


}
