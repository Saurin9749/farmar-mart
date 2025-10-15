package com.micro.question_services.Service;

import com.micro.question_services.Dao.QuestionDao;
import com.micro.question_services.model.Question;
import com.micro.question_services.model.QuestionWrapper;
import com.micro.question_services.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    public List<Question> getAllQuestions(){
        return questionDao.findAll();

    }
    public List<Question> getQuestionsByCategory(String category){
        return questionDao.findByCategory(category);
    }
    public Question addQuestion(Question question){
        return questionDao.save(question);
    }
    public String deleteQuestion(Long id){
        questionDao.deleteById(id);
        return "Question has been deleted";
    }

    public List<QuestionWrapper> getQuizQuestions(String category){
        List<Question> questions = questionDao.findByCategory(category);
        return questions.stream()
                .map(q-> new QuestionWrapper(
                        q.getId(),
                        q.getQuestionTitle(),
                        q.getOption1(),
                        q.getOption2(),
                        q.getOption3(),
                        q.getOption4())).collect(Collectors.toList());

    }

    public int calculateScore(List<Response> responses){
        int score = 0;
        for(Response response : responses){
            Optional<Question> q = questionDao.findById(response.getQuestionId());
            if(q.isPresent() && q.get().getCorrectAnswer().equalsIgnoreCase(response.getSelectedAnswer())){
                score ++;
            }
        }
        return score;
    }


}
