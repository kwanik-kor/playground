package com.gani.boot3.validation.presentation.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record ComplicatedRequestDto(
        @NotNull
        @Positive
        Long idx,

        @NotBlank
        @Length(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
        String title,

        @NotBlank
        @Length(min = 2, max = 100, message = "내용은 2자 이상 100자 이하로 입력해주세yo.")
        String content,

        @NotEmpty
        @Size(min = 1, max = 10, message = "대상은 최소 1개 최대 10개까지 선택 가능합니다.")
        List<Long> targetIdxes
) {

    // 요상한 Intellij indent 8자리 우선 방지용
    public ComplicatedRequestDto {
    }

}
