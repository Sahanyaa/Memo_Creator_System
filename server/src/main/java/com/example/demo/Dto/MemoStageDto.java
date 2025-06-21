package com.example.demo.Dto;

import com.example.demo.Enums.StageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemoStageDto {

    private int stageNumber;
    private String actionStatus;
    private String employeeName;
    private StageStatus stageStatus;

}

