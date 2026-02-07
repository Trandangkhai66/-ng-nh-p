package com.vlu.capstone.worker.processor;

import com.vlu.capstone.worker.Job;
import com.vlu.capstone.worker.JobProcessor;
import com.vlu.capstone.worker.JobType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogActivityJobProcessor implements JobProcessor {

    @Override
    public void process(Job job) {
        log.info("LOG_ACTIVITY: {}", job.getPayload());
        // TODO: persist to activity_log table if needed
    }

    @Override
    public JobType getType() {
        return JobType.LOG_ACTIVITY;
    }
}
