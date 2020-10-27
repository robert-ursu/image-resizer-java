package com.resizer.imageresizer.service;

import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.util.Optional;

public interface CacheService {
  Optional<Resource> get(String name, Integer width, Integer height);
  Resource save(String name, int width, int height, BufferedImage image);
  Optional<Long> getTotalCachedImages();
}
