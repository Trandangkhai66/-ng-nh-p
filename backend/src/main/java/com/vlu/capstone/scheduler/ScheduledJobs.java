package com.vlu.capstone.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledJobs {

    @Scheduled(cron = "0 0 2 * * ?")
    @SchedulerLock(name = "cleanupExpiredTokens", lockAtMostFor = "5m", lockAtLeastFor = "1m")
    public void cleanupExpiredTokens() {
        log.info("Scheduled: cleanupExpiredTokens");
        // TODO: clean expired refresh tokens from Redis
    }

    @Scheduled(fixedDelay = 300000)
    @SchedulerLock(name = "healthCheck", lockAtMostFor = "2m", lockAtLeastFor = "30s")
    public void healthCheck() {
        log.debug("Scheduled: healthCheck");
    }
}
