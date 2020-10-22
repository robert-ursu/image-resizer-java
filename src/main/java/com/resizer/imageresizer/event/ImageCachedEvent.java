package com.resizer.imageresizer.event;

import org.springframework.context.ApplicationEvent;

public class ImageCachedEvent extends ApplicationEvent {
  public ImageCachedEvent(Object source) {
    super(source);
  }
}
