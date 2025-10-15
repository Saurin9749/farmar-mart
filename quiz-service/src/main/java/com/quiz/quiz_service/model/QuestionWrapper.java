package com.quiz.quiz_service.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWrapper {
    private Long id;
    private String questionTitle;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
