package com.quiz.quiz_service.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Long questionId;
    private String response;
}
