package com.resizer.imageresizer.service;

import com.resizer.imageresizer.event.ImageCachedEvent;
import com.resizer.imageresizer.property.AppProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CacheServiceImpl implements CacheService {
  private final Path cachePath;
  private final ApplicationEventPublisher eventPublisher;

  public CacheServiceImpl(AppProperties appProperties, ApplicationEventPublisher eventPublisher) {
    this.cachePath = Paths
        .get(appProperties.getCachePath())
        .toAbsolutePath()
        .normalize();
    this.eventPublisher = eventPublisher;
    try {
      if (!Files.isDirectory(cachePath))
        Files.createDirectories(cachePath);
    } catch (Exception ex) {
      throw new RuntimeException("Failed to create storage directory");
    }
  }

  @Override
  public Resource save(String name, int width, int height, BufferedImage image) {
    try {
      Path targetLocation = this.cachePath.resolve(buildFileName(name, width, height));
      ImageIO.write(image, FilenameUtils.getExtension(name), Files.newOutputStream(targetLocation));

      this.eventPublisher.publishEvent(new ImageCachedEvent(this));

      return new UrlResource(targetLocation.toUri());
    } catch (IOException ex) {
      throw new RuntimeException("Failed to save image to cache");
    }
  }

  @Override
  public Optional<Resource> get(String name, Integer width, Integer height) {
    try {
      if (width == null || height == null) return Optional.empty();

      Path imageLocation = this.cachePath.resolve(buildFileName(name, width, height));
      if (Files.exists(imageLocation)) {
        return Optional.of(new UrlResource(imageLocation.toUri()));
      }
      return Optional.empty();
    } catch (MalformedURLException ex) {
      throw new RuntimeException("Invalid resource url.");
    }
  }

  @Override
  public Optional<Long> getTotalCachedImages() {
    try (Stream<Path> files = Files.list(this.cachePath)) {
      return Optional.of(files.count());
    } catch (IOException ex) {
      return Optional.empty();
    }
  }

  private static String buildFileName(String name, int width, int height) {
    String fileName = FilenameUtils.getBaseName(name);
    String extension = FilenameUtils.getExtension(name);
    return String.format("%s-%d-%d.%s", fileName, width, height, extension);
  }
}
