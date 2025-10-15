package com.micro.question_services.controller;

import com.micro.question_services.Service.QuestionService;
import com.micro.question_services.model.Question;
import com.micro.question_services.model.QuestionWrapper;
import com.micro.question_services.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/all")
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public List<Question> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("/add")
    public Question addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        return questionService.deleteQuestion(id);
    }

    @GetMapping("/generate/{category}")
    public List<QuestionWrapper> generateQuiz(@PathVariable String category) {
        return questionService.getQuizQuestions(category);
    }

    @PostMapping("/submit")
    public int submitQuiz(@RequestBody List<Response> responses) {
        return questionService.calculateScore(responses);
    }
}
