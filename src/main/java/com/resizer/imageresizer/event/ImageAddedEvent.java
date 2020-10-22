package com.resizer.imageresizer.event;

import org.springframework.context.ApplicationEvent;

public class ImageAddedEvent extends ApplicationEvent {
  public ImageAddedEvent(Object source) {
    super(source);
  }
}
