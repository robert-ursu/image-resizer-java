package com.resizer.imageresizer.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
  private String storagePath;
  private String cachePath;

  public String getStoragePath() {
    return storagePath;
  }

  public void setStoragePath(String storagePath) {
    this.storagePath = storagePath;
  }

  public String getCachePath() {
    return cachePath;
  }

  public void setCachePath(String cachePath) {
    this.cachePath = cachePath;
  }
}
