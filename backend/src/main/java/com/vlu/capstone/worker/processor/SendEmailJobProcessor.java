package com.vlu.capstone.worker.processor;

import com.vlu.capstone.worker.Job;
import com.vlu.capstone.worker.JobProcessor;
import com.vlu.capstone.worker.JobType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor // Tự động tạo Constructor để nạp JavaMailSender
public class SendEmailJobProcessor implements JobProcessor {

    private final JavaMailSender mailSender;

    @Override
    public void process(Job job) {
        log.info("Đang tiến hành gửi email thật cho: {}", job.getPayload());
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("trandangkhai66@gmail.com"); // Email gửi đi
            message.setTo(job.getPayload().toString());    // Email nhận (lấy từ Job)
            message.setSubject("Mã xác thực OTP - Capstone VLU");
            message.setText("Mã OTP của bạn là: 123456. Vui lòng không cung cấp mã này cho bất kỳ ai.");
            
            mailSender.send(message);
            log.info("✅ Đã gửi mail thành công tới {}", job.getPayload());
        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi mail: {}", e.getMessage());
        }
    }

    @Override
    public JobType getType() {
        return JobType.SEND_EMAIL;
    }
}