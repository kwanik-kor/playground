package com.gani.springai.service.structuredoutput;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReviewClassification {
    private final String review;
    private final Sentiment sentiment;

    enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE
    }
}
