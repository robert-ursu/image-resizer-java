package com.resizer.imageresizer.controller;

import com.resizer.imageresizer.dto.StatsDto;
import com.resizer.imageresizer.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
public class StatsController {
  private final StatsService statsService;

  public StatsController(StatsService statsService) {
    this.statsService = statsService;
  }

  @GetMapping()
  public ResponseEntity<StatsDto> getAllStats() {
    return ResponseEntity.ok(statsService.getStats());
  }

  @DeleteMapping()
  public ResponseEntity<?> getStat() {
    this.statsService.resetStats();
    return ResponseEntity.ok().build();
  }
}
