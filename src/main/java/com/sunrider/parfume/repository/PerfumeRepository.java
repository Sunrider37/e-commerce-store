package com.sunrider.parfume.repository;

import com.sunrider.parfume.model.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    List<Perfume> findByIdIn(List<Long> perfumeIds);

    List<Perfume> findAllByOrderByIdAsc();

    List<Perfume> findByPerfumerOrderByPriceDesc(String perfumer);

    List<Perfume> findByGenderOrderByPriceDesc(String perfumeGender);

    List< Perfume> findByPerfumerIn(List<String> perfumers);

    List<Perfume> findByGenderIn(List<String> genders);

    List<Perfume> findByPriceBetween(Integer integer, Integer integer1);
}
