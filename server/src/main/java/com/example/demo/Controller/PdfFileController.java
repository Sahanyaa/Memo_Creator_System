package com.example.demo.Controller;

import com.example.demo.Entity.PdfFile;
import com.example.demo.Repository.PdfFileRepository;
import com.example.demo.Service.PdfFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfFileController {

    private final PdfFileService pdfFileService;

    // upload pdf
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            PdfFile savedPdf = pdfFileService.savePdf(file);

            return ResponseEntity.ok("File uploaded successfully! ID: " + savedPdf.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    // pdf to user and allow to download it
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) {
        Optional<PdfFile> pdfFile = pdfFileService.getPdf(id);
        if (pdfFile.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.get().getName() + "\"")
                    .body(pdfFile.get().getData());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Optional<PdfFile> pdfFileOptional = pdfFileService.getPdf(id);

        if (pdfFileOptional.isPresent()) {
            PdfFile pdfFile = pdfFileOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.getName() + "\"")
                    .body(pdfFile.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Add user approval sign
    @PostMapping("/uploadImage")
    public ResponseEntity<byte[]> uploadPdfWithImage(@RequestParam("image") MultipartFile imageFile,
            @RequestParam("pdf") MultipartFile pdfFile,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("x") float x,
            @RequestParam("y") float y) {
        try {
            PdfFile updatedPdf = pdfFileService.addImageToPdf(imageFile, pdfFile, pageNumber, x, y);

            HttpHeaders headers = new HttpHeaders();
            // headers.add("Content-Disposition", "attachment; filename=" +
            //         updatedPdf.getName());
            headers.add("Content-Type", "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(updatedPdf.getData());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
