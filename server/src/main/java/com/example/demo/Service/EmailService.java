package com.example.demo.Service;

import com.example.demo.Entity.ForwardMemo;
import com.example.demo.Repository.ForwardMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ForwardMemoRepository forwardMemoRepository;

    @Value("${app.default-created-by}")
    private Integer defaultCreatedBy;

    public void sendEmail(String recipient, String comment , String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipient);
        mailMessage.setSubject("----  Memo creator  ------\n\n"+subject);
        mailMessage.setText(comment);
        mailSender.send(mailMessage);

        ForwardMemo forwardMemo = new ForwardMemo();
        forwardMemo.setCreatedBy(defaultCreatedBy);
        forwardMemo.setEmail(recipient);
        forwardMemo.setComment(comment);
        forwardMemo.setCreatedDate(new Date());

        forwardMemoRepository.save(forwardMemo);
    }

    public List<ForwardMemo> getForwardMemos() {
        return forwardMemoRepository.findAll();
    }

}

