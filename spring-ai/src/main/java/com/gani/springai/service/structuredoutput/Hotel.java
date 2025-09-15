package com.gani.springai.service.structuredoutput;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Hotel {
    private String city;
    private List<String> names;
}
