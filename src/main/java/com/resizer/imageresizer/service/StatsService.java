package com.resizer.imageresizer.service;

import com.resizer.imageresizer.dto.StatsDto;

public interface StatsService {
  StatsDto getStats();
  void resetStats();
}
