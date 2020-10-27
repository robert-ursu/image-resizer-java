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
import java.util.Optional;

@RestController
@RequestMapping("/image")
public class ImageController {
  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @GetMapping("{name:.+}")
  public ResponseEntity<Resource> get(
      @PathVariable String name,
      @RequestParam(required = false) Integer width,
      @RequestParam(required = false) Integer height) {

    final Optional<Resource> image = this.imageService.getImage(name, width, height);

    if (image.isEmpty())
      return ResponseEntity.notFound().build();

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(image.get().getFilename())))
        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", image.get().getFilename()))
        .body(image.get());
  }

  @PostMapping()
  public ResponseEntity<?> uploadFile(@RequestParam("image") MultipartFile image) {
    if (image == null) {
      return ResponseEntity.badRequest().build();
    }

    Resource savedImage = this.imageService.saveImage(image);

    if (savedImage == null || savedImage.getFilename() == null) {
      return ResponseEntity.badRequest().build();
    }

    URI fileDownloadUri = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/image/")
        .path(savedImage.getFilename())
        .build()
        .toUri();

    return ResponseEntity.created(fileDownloadUri).build();
  }
}
