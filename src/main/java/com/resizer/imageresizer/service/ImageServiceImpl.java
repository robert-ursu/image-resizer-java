package com.resizer.imageresizer.service;

import com.resizer.imageresizer.event.CacheHitEvent;
import com.resizer.imageresizer.event.CacheMissEvent;
import com.resizer.imageresizer.event.ImageAddedEvent;
import com.resizer.imageresizer.property.AppProperties;
import com.sun.istack.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ImageServiceImpl implements ImageService {
  private final Path storagePath;
  private final CacheService cacheService;
  private final ApplicationEventPublisher eventPublisher;

  public ImageServiceImpl(AppProperties appProperties, CacheService cacheService, ApplicationEventPublisher eventPublisher) {
    this.storagePath = Paths
        .get(appProperties.getStoragePath())
        .toAbsolutePath()
        .normalize();

    this.cacheService = cacheService;
    this.eventPublisher = eventPublisher;

    try {
      if (!Files.isDirectory(storagePath))
        Files.createDirectories(storagePath);

    } catch (Exception ex) {
      throw new RuntimeException("Failed to create storage directory");
    }
  }

  @Override
  public Resource getImage(String name, Integer width, Integer height) {
    final Resource cachedImage = this.cacheService.get(name, width, height);

    if (cachedImage != null) {
      this.eventPublisher.publishEvent(new CacheHitEvent(this));
      return cachedImage;
    }

    this.eventPublisher.publishEvent(new CacheMissEvent(this));

    Resource resource = getImageFromStorage(name);
    if (resource == null) return null;

    BufferedImage resizedImaged = getResizedImage(resource, width, height);

    return this.cacheService.save(name, resizedImaged.getWidth(), resizedImaged.getHeight(), resizedImaged);
  }

  @Override
  public String saveImage(@NotNull MultipartFile image) {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));

    try {
      if (fileName.contains("..")) {
        throw new RuntimeException("Invalid image name");
      }

      Path targetLocation = this.storagePath.resolve(fileName);
      Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      this.eventPublisher.publishEvent(new ImageAddedEvent(this));

      return fileName;
    } catch (IOException ex) {
      throw new RuntimeException("Could not save image.");
    }
  }

  @Override
  public Long getImagesCount() {
    try (Stream<Path> files = Files.list(this.storagePath)) {
      return files.count();
    } catch (IOException ex) {
      return null;
    }
  }

  @Nullable
  private Resource getImageFromStorage(String name) {
    try {
      Path imagePath = this.storagePath.resolve(name).normalize();
      Resource resource = new UrlResource(imagePath.toUri());

      if (!resource.exists()) {
        return null;
      }

      return resource;
    } catch (MalformedURLException ex) {
      return null;
    }
  }

  private BufferedImage getResizedImage(Resource resource, Integer width, Integer height) {
    try {
      BufferedImage image = ImageIO.read(resource.getInputStream());
      int finalWidth = width == null ? image.getWidth() : width;
      int finalHeight = height == null ? image.getHeight() : height;

      if (image.getWidth() == finalWidth && image.getHeight() == finalHeight) {
        return image;
      }

      return resizeImage(image, finalWidth, finalHeight);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to resize the image.");
    }
  }

  private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
    BufferedImage resizedImage = new BufferedImage(width, height, image.getType());

    Graphics2D graphics = resizedImage.createGraphics();
    graphics.drawImage(image, 0, 0, width, height, null);
    graphics.setComposite(AlphaComposite.Src);
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.dispose();

    return resizedImage;
  }
}
