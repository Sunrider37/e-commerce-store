package com.sunrider.parfume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PerfumeRequest {

    private Long id;

    private String filename;

    @Length(max = 250)
    private String perfumeTitle;
    @Length(max = 255)
    private String perfumer;

    private Integer year;

    @Length(max = 255)
    private String country;

    @Length(max = 255)
    private String perfumeGender;

    @Length(max = 255)
    private String fragranceTopNotes;

    @Length(max = 255)
    private String fragranceMiddleNotes;

    @Length(max = 255)
    private String fragranceBaseNotes;

    private Integer price;

    @Length(max = 255)
    private String volume;

    @Length(max = 255)
    private String type;
}
