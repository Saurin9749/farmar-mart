package com.quiz.quiz_service.service;

import com.quiz.quiz_service.model.QuestionWrapper;
import com.quiz.quiz_service.model.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    ResponseEntity<String> createQuiz(String category, int numOfQues, String title);
    ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Long id);
    ResponseEntity<Integer> submitQuiz(Long id, List<Response> responses);
}
