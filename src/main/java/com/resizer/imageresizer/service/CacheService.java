package com.resizer.imageresizer.service;

import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;

public interface CacheService {
  Resource save(String name, int width, int height, BufferedImage image);
  Resource get(String name, Integer width, Integer height);
  Long getTotalCachedImages();
}
