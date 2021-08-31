package com.sunrider.parfume.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sunrider.parfume.model.Perfume;
import com.sunrider.parfume.repository.PerfumeRepository;
import com.sunrider.parfume.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final ReviewRepository reviewRepository;
    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${amazon.s3.bucket.name")
    private String bucketName;

    public Perfume savePerfume(Perfume perfume, MultipartFile file){
        if(file == null) {
            perfume.setFilename(amazonS3Client.getUrl(bucketName, "empty.jpg").toString());
        }else{
            File file1 = new File(Objects.requireNonNull(file.getOriginalFilename()));
            try{
                FileOutputStream stream = new FileOutputStream(file1);
                stream.write(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
            amazonS3Client.putObject(new PutObjectRequest(bucketName,fileName,file1));
            perfume.setFilename(amazonS3Client.getUrl(bucketName,fileName).toString());
            file1.delete();
        }
        return perfumeRepository.save(perfume);
    }

    public List<Perfume> filter(List<String> perfumers, List<String> genders, List<Integer> prices, boolean sortByPrice) {
        List<Perfume> perfumeList = new ArrayList<>();

        if (!perfumers.isEmpty() || !genders.isEmpty() || !prices.isEmpty()) {
            if (!perfumers.isEmpty()) {
                perfumeList.addAll(perfumeRepository.findByPerfumerIn(perfumers));
            }
            if (!genders.isEmpty()) {
                if (!perfumeList.isEmpty()) {
                    List<Perfume> gendersList = new ArrayList<>();
                    for (String gender : genders) {
                        gendersList.addAll(perfumeList.stream()
                                .filter(perfume -> perfume.getGender().equals(gender))
                                .collect(Collectors.toList()));
                    }
                    perfumeList = gendersList;
                } else {
                    perfumeList.addAll(perfumeRepository.findByGenderIn(genders));
                }
            }
            if (!prices.isEmpty()) {
                perfumeList = perfumeRepository.findByPriceBetween(prices.get(0), prices.get(1));
            }
        } else {
            perfumeList = perfumeRepository.findAllByOrderByIdAsc();
        }
        if (sortByPrice) {
            perfumeList.sort(Comparator.comparing(Perfume::getPrice));
        }
        return perfumeList;
    }

    @Transactional(readOnly = true)
    public List<Perfume> findAllPerfumes(){
        return perfumeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Perfume> findPerfumesByIds(List<Long> perfumeIds){
        return perfumeRepository.findByIdIn(perfumeIds);
    }

    @Transactional(readOnly = true)
    public Perfume findPerfumeById(Long id){
        return perfumeRepository.findById(id).orElseThrow(() ->
               new RuntimeException("File not found"));
    }

    public List<Perfume> deletePerfume(Long perfumeId){
        Perfume perfume = perfumeRepository.findById(perfumeId).orElseThrow();
        perfume.getReviews().forEach(review -> reviewRepository.deleteById(review.getId()));
        perfumeRepository.delete(perfume);
        return perfumeRepository.findAllByOrderByIdAsc();
    }

    public List<Perfume> findByPerfumerOrderByPriceDesc(String perfumer) {
        return perfumeRepository.findByPerfumerOrderByPriceDesc( perfumer);
    }

    public List<Perfume> findByPerfumeGenderOrderByPriceDesc(String perfumeGender) {
        return perfumeRepository.findByGenderOrderByPriceDesc(perfumeGender);
    }


}
