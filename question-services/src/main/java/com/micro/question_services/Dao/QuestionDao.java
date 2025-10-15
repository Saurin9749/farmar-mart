package com.micro.question_services.Dao;

import com.micro.question_services.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionDao extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);
}
