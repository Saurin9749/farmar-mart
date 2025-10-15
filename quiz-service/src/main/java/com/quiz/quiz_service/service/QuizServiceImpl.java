package com.quiz.quiz_service.service;

import com.quiz.quiz_service.dao.QuizDao;
import com.quiz.quiz_service.model.QuestionWrapper;
import com.quiz.quiz_service.model.Quiz;
import com.quiz.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizDao quizDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> createQuiz(String category, int numOfQues, String title) {
        // call question-service to get random questions
        String url = "http://QUESTION-SERVICE/question/generate?category=" + category + "&numQ=" + numOfQues;
        ResponseEntity<List> questionsResponse = restTemplate.exchange(
                url, HttpMethod.GET, null, List.class);

        if (questionsResponse.getBody() == null || questionsResponse.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No questions found for category " + category);
        }

        Quiz quiz = Quiz.builder()
                .title(title)
                .category(category)
                .numOfQuestions(numOfQues)
                .build();

        quizDao.save(quiz);
        return ResponseEntity.ok("Quiz created successfully with ID: " + quiz.getId());
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Long id) {
        Optional<Quiz> quizOpt = quizDao.findById(id);
        if (quizOpt.isEmpty()) return ResponseEntity.notFound().build();

        String url = "http://QUESTION-SERVICE/question/getQuestionsForQuiz/" + id;
        List<QuestionWrapper> questions = restTemplate.getForObject(url, List.class);
        return ResponseEntity.ok(questions);
    }

    @Override
    public ResponseEntity<Integer> submitQuiz(Long id, List<Response> responses) {
        return null;
    }

    @Override
    public ResponseEntity<Integer> submitQuiz(Long id, List<Response> responses) {
        String url = "http://QUESTION-SERVICE/question/getScore";
        ResponseEntity<Integer> scoreResponse =
                restTemplate.postForEntity(url, responses, Integer.class);
        return ResponseEntity.ok(scoreResponse.getBody());
    }
}
