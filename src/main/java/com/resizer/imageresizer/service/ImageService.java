package com.resizer.imageresizer.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  Resource getImage(String name, Integer width, Integer height);
  String saveImage(MultipartFile file);
  Long getImagesCount();
}
