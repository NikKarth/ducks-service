package edu.iu.habahram.ducksservice.repository;

import edu.iu.habahram.ducksservice.model.DuckEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuckRepository extends JpaRepository<DuckEntity, Integer> {
}