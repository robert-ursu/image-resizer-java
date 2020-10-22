package com.resizer.imageresizer.service;

import com.resizer.imageresizer.constant.StatConstants;
import com.resizer.imageresizer.dto.StatsDto;
import com.resizer.imageresizer.model.Stat;
import com.resizer.imageresizer.repository.StatsRepository;
import org.springframework.stereotype.Component;

@Component
public class StatsServiceImpl implements StatsService{
  private final StatsRepository statsRepository;
  private final ImageService imageService;
  private final CacheService cacheService;

  public StatsServiceImpl(StatsRepository statsRepository,
                          ImageService imageService,
                          CacheService cacheService) {
    this.statsRepository = statsRepository;
    this.imageService = imageService;
    this.cacheService = cacheService;
  }

  @Override
  public StatsDto getStats() {
    Stat cacheHit = this.statsRepository.findById(StatConstants.CACHE_HIT)
        .orElse(Stat.empty(StatConstants.CACHE_HIT));
    Stat cacheMiss = this.statsRepository.findById(StatConstants.CACHE_MISS)
        .orElse(Stat.empty(StatConstants.CACHE_MISS));
    Stat totalImages = this.statsRepository.findById(StatConstants.TOTAL_IMAGES)
        .orElse(Stat.empty(StatConstants.TOTAL_IMAGES));
    Stat totalCachedImages = this.statsRepository.findById(StatConstants.TOTAL_CACHED_IMAGES)
        .orElse(Stat.empty(StatConstants.TOTAL_CACHED_IMAGES));

    return new StatsDto()
        .setCacheHit(cacheHit.getValue())
        .setCacheMiss(cacheMiss.getValue())
        .setTotalImages(totalImages.getValue())
        .setTotalCachedImages(totalCachedImages.getValue());
  }

  @Override
  public void resetStats() {
    this.statsRepository.save(Stat.empty(StatConstants.CACHE_HIT));
    this.statsRepository.save(Stat.empty(StatConstants.CACHE_MISS));
    this.statsRepository.save(new Stat(StatConstants.TOTAL_IMAGES, this.imageService.getImagesCount()));
    this.statsRepository.save(new Stat(StatConstants.TOTAL_CACHED_IMAGES, this.cacheService.getTotalCachedImages()));
    this.statsRepository.flush();
  }
}
