package com.example.demo.Entity;

import com.example.demo.Enums.StageStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressionPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stageNumber;
    private String action;
    @Column(name = "user_id")
    private Long userId; // Or a User object if you have a User entity

    @Column(name = "user_name")
    private String userName;

    @Enumerated(EnumType.STRING)
    private StageStatus userStatus = StageStatus.PENDING;

    @Column(name = "user_comment")
    private String Comment;

    @Lob
    @Column(name = "user_signature", columnDefinition = "LONGBLOB")
    private byte[] userSignature;

    @ManyToOne
    @JoinColumn(name = "memo_id")
    private Memo memo;
}
