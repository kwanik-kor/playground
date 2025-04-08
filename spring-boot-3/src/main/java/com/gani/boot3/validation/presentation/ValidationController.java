package com.gani.boot3.validation.presentation;

import com.gani.boot3.validation.application.NormalValidationService;
import com.gani.boot3.validation.presentation.dto.request.ComplicatedRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/validation")
@RestController
public class ValidationController {
    private final NormalValidationService normalValidationService;

    @PostMapping("/normal")
    public ResponseEntity<Void> insertValidation(@RequestBody @Valid ComplicatedRequestDto request) {
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/strict")
    public ResponseEntity<Void> insertWithStrictValidation(@RequestBody @Valid ComplicatedRequestDto request) {
        return ResponseEntity.accepted().build();
    }

}
