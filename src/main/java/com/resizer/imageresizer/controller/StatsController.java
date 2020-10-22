package com.resizer.imageresizer.controller;

import com.resizer.imageresizer.dto.StatsDto;
import com.resizer.imageresizer.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StatsController {
  private final StatsService statsService;

  public StatsController(StatsService statsService) {
    this.statsService = statsService;
  }

  @GetMapping("/stats")
  public ResponseEntity<StatsDto> GetAllStats() {
    return ResponseEntity.ok(statsService.getStats());
  }

  @DeleteMapping("/stats")
  public ResponseEntity<?> GetStat() {
    this.statsService.resetStats();
    return ResponseEntity.ok().build();
  }
}
