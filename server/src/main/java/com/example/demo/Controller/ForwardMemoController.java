package com.example.demo.Controller;

import com.example.demo.Entity.ForwardMemo;
import com.example.demo.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
@CrossOrigin(origins = "http://localhost:5173")
public class ForwardMemoController {

    private final EmailService emailService;

    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sendEmail(@RequestBody ForwardMemo forwardMemo) {
        emailService.sendEmail(forwardMemo.getEmail(),forwardMemo.getComment(),forwardMemo.getSubject());
        return ResponseEntity.ok("Email Sent Successfully!");
    }

    @GetMapping("/forwardMemos")
    public List<ForwardMemo> getAll() {
        return emailService.getForwardMemos();
    }
}