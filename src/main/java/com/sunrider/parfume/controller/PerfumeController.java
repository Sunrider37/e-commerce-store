package com.sunrider.parfume.controller;

import com.sunrider.parfume.dto.PerfumeResponse;
import com.sunrider.parfume.mapper.PerfumeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/perfumes")
public class PerfumeController {

    private final PerfumeMapper perfumeMapper;

    @GetMapping
    public ResponseEntity<List<PerfumeResponse>> getAllPerfumes() {
        return ResponseEntity.ok(perfumeMapper.findAllPerfumes());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PerfumeResponse> getPerfumeById(@PathVariable("id") Long perfumeId) {
        return ResponseEntity.ok(perfumeMapper.findPerfumeById(perfumeId));
    }


}
