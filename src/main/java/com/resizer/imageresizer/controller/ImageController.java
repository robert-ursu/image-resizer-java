package com.resizer.imageresizer.controller;

import com.resizer.imageresizer.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URLConnection;

@RestController
public class ImageController {
  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @GetMapping("/image/{name:.+}")
  public ResponseEntity<Resource> Get(
      @PathVariable String name,
      @RequestParam(required = false) Integer width,
      @RequestParam(required = false) Integer height) {

    final Resource image = this.imageService.getImage(name, width, height);

    if (image == null)
      return ResponseEntity.notFound().build();

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(image.getFilename())))
        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", image.getFilename()))
        .body(image);
  }

  @PostMapping("/image")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile image) {
    String fileName = this.imageService.saveImage(image);

    URI fileDownloadUri = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/image/")
        .path(fileName)
        .build()
        .toUri();

    return ResponseEntity.created(fileDownloadUri).build();
  }
}
