package com.crio.learning_navigator.dto;

import java.util.List;

import com.crio.learning_navigator.entity.StudentEntity;
import com.crio.learning_navigator.entity.SubjectEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDto {
    private Long subjectId;
    
}
