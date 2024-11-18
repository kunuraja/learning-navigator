package com.crio.learning_navigator.dto;


import java.util.ArrayList;
import java.util.List;
import com.crio.learning_navigator.entity.ExamEntity;
import com.crio.learning_navigator.entity.SubjectEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEnrollmentDto {

    private Long studentId;
    private String studentName;
    private  List<EnrolledSubjectsDto> enrolledSubjects = new ArrayList<>();
   // private  List<ExamEntity> registeredExams = new ArrayList<>();

    
}
