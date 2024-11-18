package com.crio.learning_navigator.exchange;

import java.util.ArrayList;
import java.util.List;

import com.crio.learning_navigator.entity.StudentEntity;
import com.crio.learning_navigator.entity.SubjectEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterExamResponse {

    private String examId;
    private SubjectEntity subject;
    private List<Long> enrolledStudents = new ArrayList<>();
    
    
}
