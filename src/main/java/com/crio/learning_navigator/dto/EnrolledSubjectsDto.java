package com.crio.learning_navigator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolledSubjectsDto {
    private Long subjectId;
    private String subjectName;
    
}
