package com.example.demo.Dto;

import com.example.demo.Enums.MemoStatus;
import com.example.demo.Enums.StageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemoDto {

    private Long id;
    private Integer memoCreatorId;
    private String memoCreator;
    private String subject;
    private String description;
    private String comments;
    private MemoStatus memoStatus;
    private Date createdDate;
    private List<ProgressionPathDto> progressionPathDtos;
    private String downloadUrl;

}
