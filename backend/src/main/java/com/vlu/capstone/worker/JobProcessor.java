package com.vlu.capstone.worker;

public interface JobProcessor {
    void process(Job job);
    JobType getType();
}
