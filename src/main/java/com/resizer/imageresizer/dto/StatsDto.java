package com.resizer.imageresizer.dto;

public class StatsDto {
  private Long cacheHit;
  private Long cacheMiss;
  private Long totalImages;
  private Long totalCachedImages;

  public Long getCacheHit() {
    return cacheHit;
  }

  public StatsDto setCacheHit(Long cacheHit) {
    this.cacheHit = cacheHit;
    return this;
  }

  public Long getCacheMiss() {
    return cacheMiss;
  }

  public StatsDto setCacheMiss(Long cacheMiss) {
    this.cacheMiss = cacheMiss;
    return this;
  }

  public Long getTotalImages() {
    return totalImages;
  }

  public StatsDto setTotalImages(Long totalImages) {
    this.totalImages = totalImages;
    return this;
  }

  public Long getTotalCachedImages() {
    return totalCachedImages;
  }

  public StatsDto setTotalCachedImages(Long resizedImages) {
    this.totalCachedImages = resizedImages;
    return this;
  }
}
