package com.vlu.capstone.worker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job implements Serializable {
    private String id;
    private JobType type;
    private Map<String, Object> payload;
    private int retryCount;

    public void incrementRetry() {
        this.retryCount++;
    }
}
