package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressionPathDto {

    private Long id;
    private int stageNumber;
    private String action;
    private Long userId;
    private String userName;

}