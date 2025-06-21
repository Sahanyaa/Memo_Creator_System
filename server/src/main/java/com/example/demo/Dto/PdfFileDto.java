package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PdfFileDto {

    private Long id;
    private String name;
    private String type;
    private byte[] data;
    private String downloadUrl;

}

