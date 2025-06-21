package com.example.demo.Controller;

import com.example.demo.Dto.MemoDto;
import com.example.demo.Entity.Memo;
import com.example.demo.Entity.ProgressionPath;
import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.Service.MemoService;
import com.example.demo.Service.PdfFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/MemoCreator")
@CrossOrigin(origins = "http://localhost:5173")
// Enable CORS for the frontend URL (React app running on localhost:3000)
public class MemoController {

    private final MemoService memoService;
    private final PdfFileService pdfFileService;
    private final EmployeeRepository employeeRepository;

    // Endpoint to create a new memo
    @PostMapping("/create")
    public ResponseEntity<MemoDto> uploadMemo(
            @RequestParam("subject") String subject,
            @RequestParam("description") String description,
            @RequestParam("comments") String comments,
            @RequestParam("file") MultipartFile file,
            @RequestParam("creatorSignature") MultipartFile creatorSignature,
            @RequestParam("progressionPath") String progressionPathJson) {

        try {

            // Save the memo using the service
            Memo savedMemo = memoService.saveMemo(subject, description, comments, file, progressionPathJson,
                    creatorSignature);

            // Convert the saved memo to DTO and return
            MemoDto memoDto = memoService.convertToDto(savedMemo);

            return ResponseEntity.ok(memoDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    // Endpoint to get a memo by its ID
    @GetMapping("/{id}")
    public ResponseEntity<MemoDto> getMemo(@PathVariable Long id) {
        Optional<Memo> memo = memoService.getMemo(id);
        return memo.map(value -> ResponseEntity.ok(memoService.convertToDto(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    // MyMemo-API
    @GetMapping("/myMemos")
    public List<MemoDto> getAllMemos() {
        return memoService.getAllMemos();
    }

    @GetMapping("/myActions")
    public List<?> getMyActionsMemos() {
        return memoService.getMyActionsMemos(memoService.getAllMemos());
    }

    //Memo Progress
    @GetMapping("/memoProgress/{memoId}")
    public List<ProgressionPath> getProgressByMemoId(@PathVariable Long memoId) {
        return memoService.getProgressByMemoId(memoId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
            @RequestParam Boolean isApproved,
            @RequestParam String comment,
            @RequestParam MultipartFile userSignature) {

        try {

            boolean updated = memoService.updateStatus(id, isApproved, comment, userSignature);
            if (updated) {
                return ResponseEntity.ok("Status updated successfully.");
            } else {
                return ResponseEntity.badRequest().body("Failed to update status.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
