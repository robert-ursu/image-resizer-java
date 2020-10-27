package com.resizer.imageresizer.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ImageService {
  Optional<Resource> getImage(String name, Integer width, Integer height);
  Resource saveImage(MultipartFile file);
  Optional<Long> getImagesCount();
}
