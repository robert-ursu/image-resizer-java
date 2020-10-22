package com.resizer.imageresizer.repository;

import com.resizer.imageresizer.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepository extends JpaRepository<Stat, String> {
}
