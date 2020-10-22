package com.resizer.imageresizer.event;

import org.springframework.context.ApplicationEvent;

public class CacheMissEvent extends ApplicationEvent {
  public CacheMissEvent(Object source) {
    super(source);
  }
}
