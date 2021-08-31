package com.sunrider.parfume.mapper;

import com.sunrider.parfume.dto.PerfumeRequest;
import com.sunrider.parfume.dto.PerfumeResponse;
import com.sunrider.parfume.model.Perfume;
import com.sunrider.parfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PerfumeMapper {

    private final ModelMapper mapper;
    private final PerfumeService perfumeService;

    Perfume convertToEntity(PerfumeRequest perfumeRequest){
        return mapper.map(perfumeRequest,Perfume.class);
    }

    PerfumeResponse convertToDtoResponse(Perfume perfume){
        return mapper.map(perfume,PerfumeResponse.class);
    }

    List<PerfumeResponse> convertListToResponseDto(List<Perfume> perfumes){
        return perfumes.stream().map(this::convertToDtoResponse).collect(Collectors.toList());
    }

    public PerfumeResponse findPerfumeById(Long perfumeId){
        return convertToDtoResponse(perfumeService.findPerfumeById(perfumeId));
    }

    public List<PerfumeResponse> findPerfumesByIds(List<Long> perfumesId) {
        return convertListToResponseDto(perfumeService.findPerfumesByIds(perfumesId));
    }
    public List<PerfumeResponse> findAllPerfumes(){
        return convertListToResponseDto(perfumeService.findAllPerfumes());
    }

    public List<PerfumeResponse> findByPerfumerOrderByPriceDesc(String perfumer) {
        return convertListToResponseDto(perfumeService.findByPerfumerOrderByPriceDesc(perfumer));
    }

    public List<PerfumeResponse> findByPerfumeGenderOrderByPriceDesc(String perfumeGender) {
        return convertListToResponseDto(perfumeService.findByPerfumeGenderOrderByPriceDesc(perfumeGender));
    }

    public PerfumeResponse savePerfume(PerfumeRequest perfumeRequest, MultipartFile file){
        return convertToDtoResponse(perfumeService.savePerfume(convertToEntity(
                perfumeRequest),file
        ));
    }

    public List<PerfumeResponse> deleteOrder(Long perfumeId) {
        return convertListToResponseDto(perfumeService.deletePerfume(perfumeId));
    }
}
