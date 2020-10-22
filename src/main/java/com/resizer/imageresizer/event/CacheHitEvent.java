package com.resizer.imageresizer.event;

import org.springframework.context.ApplicationEvent;

public class CacheHitEvent extends ApplicationEvent {
  public CacheHitEvent(Object source) {
    super(source);
  }
}
