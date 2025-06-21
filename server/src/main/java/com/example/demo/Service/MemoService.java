package com.example.demo.Service;

import com.example.demo.Dto.MemoDto;
import com.example.demo.Dto.ProgressionPathDto;
import com.example.demo.Enums.StageStatus;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.utils.CustomMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final PdfFileRepository pdfFileRepository;
    private final MemoStageRepository memoStageRepository;
    private final ProgressionPathRepository progressionPathRepository;
    private final PdfFileService pdfFileService;
    private final EmployeeRepository employeeRepository;

    @Value("${app.default-created-by}")
    private Integer defaultCreatedBy;




    public Memo saveMemo(String subject, String description, String comments, MultipartFile file,
            String progressionPathJson, MultipartFile creatorSignature) throws Exception {

        //add creator signature to pdf file
        PdfFile signedPDFFile = pdfFileService.addMemoCreatorSign(creatorSignature, file);

        MultipartFile signedMultipartFile = new CustomMultipartFile(
                signedPDFFile.getName(),
                signedPDFFile.getName(),
                signedPDFFile.getType(),
                signedPDFFile.getData());

        //save pdf
        PdfFile pdfFile = new PdfFile();
        pdfFile.setName(signedMultipartFile.getOriginalFilename());
        pdfFile.setType(signedMultipartFile.getContentType());
        pdfFile.setData(signedMultipartFile.getBytes());

        PdfFile savedPdf = pdfFileRepository.save(pdfFile);

        String downloadUrl = "http://localhost:8080/pdf/download/" + savedPdf.getId();

        // Convert JSON string to List<ProgressionPath>
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProgressionPath> progressionPathList = objectMapper.readValue(progressionPathJson,
                new TypeReference<List<ProgressionPath>>() {
                });

        String employee = pdfFileService.findById(Long.valueOf(defaultCreatedBy));

        // Save Memo
        byte[] creatorSignatureBytes = creatorSignature.getBytes();
        Memo memo = new Memo();

        memo.setMemoCreatorId(defaultCreatedBy);
        memo.setMemoCreator(employee);
        memo.setSubject(subject);
        memo.setDescription(description);
        memo.setComments(comments);
        memo.setCreatedDate(new Date());
        memo.setDownloadUrl(downloadUrl);
        memo.setPdfFile(savedPdf);
        memo.setCreatorSignature(creatorSignatureBytes);
        memo.setProgressionPath(new ArrayList<>());

        memoRepository.save(memo);

        for (ProgressionPath path : progressionPathList) {
            path.setMemo(memo);
            progressionPathRepository.save(path);
        }

        return memo;
    }

    public Optional<Memo> getMemo(Long memoId) {
        return memoRepository.findById(memoId);
    }

    // Helper method to convert ProgressionPath to ProgressionPathDto
    private ProgressionPathDto convertToProgressionPathDto(ProgressionPath progressionPath) {
        return new ProgressionPathDto(
                progressionPath.getId(), // Assuming ProgressionPath has getId()
                progressionPath.getStageNumber(),
                progressionPath.getAction(),
                progressionPath.getUserId(),// Or employee info, based on your ProgressionPath entity
                progressionPath.getUserName()
        );
    }

    public MemoDto convertToDto(Memo memo) {
        String downloadUrl = "http://localhost:8080/pdf/download/" + memo.getPdfFile().getId();

        // Convert ProgressionPath to DTOs
        List<ProgressionPathDto> progressionPathDtos = memo.getProgressionPath().stream()
                .map(this::convertToProgressionPathDto)
                .collect(Collectors.toList());

        return new MemoDto(
                memo.getId(),
                memo.getMemoCreatorId(),
                memo.getMemoCreator(),
                memo.getSubject(),
                memo.getDescription(),
                memo.getComments(),
                memo.getMemoStatus(),
                memo.getCreatedDate(),
                progressionPathDtos,
                downloadUrl);
    }

    public List<MemoDto> getAllMemos() {
        return memoRepository.findByMemoCreatorId(defaultCreatedBy).stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProgressionPath> getMyActionsMemos(List<MemoDto> allMemos) {
        return progressionPathRepository.findByUserId(Long.valueOf(defaultCreatedBy));
    }

    public boolean updateStatus(Long progressionPathId, Boolean isApproved, String comment, MultipartFile userSignature)
            throws Exception {
        String newUserStatus = "PENDING";
        if (isApproved) {
            newUserStatus = "COMPLETED";
        }
        Optional<ProgressionPath> optionalProgressionPath = progressionPathRepository.findById(progressionPathId);
        if (optionalProgressionPath.isPresent()) {
            ProgressionPath progressionPath = optionalProgressionPath.get();
            progressionPath.setUserStatus(StageStatus.valueOf(newUserStatus));
            progressionPath.setComment(comment);
            // Convert creatorSignature MultipartFile to byte[]
            byte[] userSignatureBytes = userSignature.getBytes();

            progressionPath.setUserSignature(userSignatureBytes);
            progressionPathRepository.save(progressionPath);
            return true;
        }
        return false;
    }

    public List<ProgressionPath> getProgressByMemoId(Long memoId) {
        return progressionPathRepository.findByMemoId(memoId);
    }




}
