package com.crio.learning_navigator.service;

import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.dto.StudentEnrollmentDto;
import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.exception.StudentNotRegisteredInSubjectException;
import com.crio.learning_navigator.exception.StudentOrSubjectDoesntExistException;
import com.crio.learning_navigator.exception.StudentOrSubjectorExamDoesntExistException;
import com.crio.learning_navigator.exchange.AddExamResponse;
import com.crio.learning_navigator.exchange.AddStudentResponse;
import com.crio.learning_navigator.exchange.AddSubjectResponse;
import com.crio.learning_navigator.exchange.RegisterExamResponse;

public interface LearningNavigatorService {
    // LearningNavigatorResponse registerStudentService(ExamDto examDto);
    AddSubjectResponse addSubjectService(SubjectDto subjectDto);

    AddStudentResponse addStudentService(StudentDto studentDto);

    StudentEnrollmentDto enrollSubjectService(Long studentId, Long subjectId)
            throws StudentOrSubjectDoesntExistException;

    AddExamResponse addExamService(// ExamDto examDto,
            Long subjectId);

    RegisterExamResponse registerExamService(Long studentId, Long subjectId, String examId)
            throws StudentOrSubjectorExamDoesntExistException, StudentNotRegisteredInSubjectException;

}
