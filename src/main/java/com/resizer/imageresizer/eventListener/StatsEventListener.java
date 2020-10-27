package com.resizer.imageresizer.eventListener;

import com.resizer.imageresizer.constant.StatConstants;
import com.resizer.imageresizer.event.CacheHitEvent;
import com.resizer.imageresizer.event.CacheMissEvent;
import com.resizer.imageresizer.event.ImageAddedEvent;
import com.resizer.imageresizer.event.ImageCachedEvent;
import com.resizer.imageresizer.model.Stat;
import com.resizer.imageresizer.repository.StatsRepository;
import com.resizer.imageresizer.service.CacheService;
import com.resizer.imageresizer.service.ImageService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StatsEventListener {
  private final StatsRepository statsRepository;
  private final ImageService imageService;
  private final CacheService cacheService;

  public StatsEventListener(StatsRepository statsRepository,
                            ImageService imageService,
                            CacheService cacheService) {
    this.statsRepository = statsRepository;
    this.imageService = imageService;
    this.cacheService = cacheService;
  }

  @Async
  @EventListener
  public void onApplicationEvent(CacheHitEvent event) {
    // this is not thread safe
    // needs to be made atomic
    Stat cacheHitStat = this.statsRepository.findById(StatConstants.CACHE_HIT)
        .orElse(Stat.empty(StatConstants.CACHE_HIT));
    cacheHitStat.setValue(cacheHitStat.getValue() + 1);
    this.statsRepository.saveAndFlush(cacheHitStat);
  }

  @Async
  @EventListener
  public void onApplicationEvent(CacheMissEvent event) {
    // this is not thread safe
    // needs to be made atomic
    Stat cacheHitStat = this.statsRepository.findById(StatConstants.CACHE_MISS)
        .orElse(Stat.empty(StatConstants.CACHE_MISS));
    cacheHitStat.setValue(cacheHitStat.getValue() + 1);
    this.statsRepository.saveAndFlush(cacheHitStat);
  }

  @Async
  @EventListener
  public void onApplicationEvent(ImageAddedEvent event) {
    // this is not thread safe
    // needs to be made atomic
    Stat cacheHitStat = this.statsRepository.findById(StatConstants.TOTAL_IMAGES)
        .orElse(Stat.empty(StatConstants.TOTAL_IMAGES));
    cacheHitStat.setValue(this.imageService.getImagesCount().orElse(0L));
    this.statsRepository.saveAndFlush(cacheHitStat);
  }

  @Async
  @EventListener
  public void onApplicationEvent(ImageCachedEvent event) {
    // this is not thread safe
    // needs to be made atomic
    Stat cacheHitStat = this.statsRepository.findById(StatConstants.TOTAL_CACHED_IMAGES)
        .orElse(Stat.empty(StatConstants.TOTAL_CACHED_IMAGES));
    cacheHitStat.setValue(this.cacheService.getTotalCachedImages().orElse(0L));
    this.statsRepository.saveAndFlush(cacheHitStat);
  }
}
