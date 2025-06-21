package com.example.demo.Entity;

import com.example.demo.Enums.MemoStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "memos")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer memoCreatorId;

    private String memoCreator;

    //Below data's get from user inputs
    private String subject;

    @Lob
    @Column(name = "creator_signature")
    private byte[] creatorSignature;

    @Column(length = 500)
    private String description;

    @Column(length = 500)
    private String comments;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String downloadUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pdf_file_id", referencedColumnName = "id")
    private PdfFile pdfFile;

    @Enumerated(EnumType.STRING)
    private MemoStatus memoStatus = MemoStatus.PENDING;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "progression_stage_id")
    private List<ProgressionPath> progressionPath;



//    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<MemoStage> stages = new ArrayList<>(); // Initialize to prevent null

//    @Enumerated(EnumType.STRING)
//    private StageStatus status = StageStatus.PENDING;

//    public void advanceStage() {
//        for (MemoStage stage : stages) {
//            if (stage.getStatus() == StageStatus.PENDING) {
//                stage.setStatus(StageStatus.APPROVED);
//                break;
//            }
//        }
//        if (stages.stream().allMatch(s -> s.getStatus() == StageStatus.APPROVED)) {
//            this.status = MemoStatus.APPROVED;
//        }
//
//    }
}

