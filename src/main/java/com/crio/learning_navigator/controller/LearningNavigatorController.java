package com.crio.learning_navigator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crio.learning_navigator.dto.ExamDto;
import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.dto.StudentEnrollmentDto;
import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.exception.StudentNotRegisteredInSubjectException;
import com.crio.learning_navigator.exception.StudentOrSubjectDoesntExistException;
import com.crio.learning_navigator.exception.StudentOrSubjectorExamDoesntExistException;
import com.crio.learning_navigator.exchange.AddExamResponse;
import com.crio.learning_navigator.exchange.AddStudentResponse;
import com.crio.learning_navigator.exchange.AddSubjectResponse;
import com.crio.learning_navigator.exchange.LearningNavigatorResponse;
import com.crio.learning_navigator.exchange.RegisterExamResponse;
import com.crio.learning_navigator.service.LearningNavigatorService;

@RestController
public class LearningNavigatorController {

    private LearningNavigatorService learningNavigatorService;

    public LearningNavigatorController(LearningNavigatorService learningNavigatorService) {
        this.learningNavigatorService = learningNavigatorService;
    }

    // @PostMapping(value = "/exams/{examId}")
    // public ResponseEntity<?> registerStudent(@PathVariable String examId,
    // @RequestBody ExamDto examDto){
    // examDto.setExamId(examId);
    // LearningNavigatorResponse learningNavigatorResponse =
    // learningNavigatorService.registerStudentService(examDto);
    // return
    // ResponseEntity.status(HttpStatus.CREATED).body(learningNavigatorResponse);

    // }

    @PostMapping(value = "/subjects")
    public ResponseEntity<?> addSubject(@RequestBody SubjectDto subjectDto) {
        try {
            AddSubjectResponse addSubjectResponse = learningNavigatorService.addSubjectService(subjectDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addSubjectResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Subject not saved");
        }

    }

    @PostMapping(value = "/students")
    public ResponseEntity<?> addStudent(@RequestBody StudentDto studentDto) {
        try {

            AddStudentResponse addStudentResponse = learningNavigatorService.addStudentService(studentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addStudentResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student not  saved");
        }

    }

    @PutMapping(value = "/students/{studentId}/subjects/{subjectId}")
    public ResponseEntity<?> enrollSubject(@PathVariable Long studentId, @PathVariable Long subjectId) {
        StudentEnrollmentDto studentEnrollmentDto;
        try {
            studentEnrollmentDto = learningNavigatorService.enrollSubjectService(studentId, subjectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(studentEnrollmentDto);
        } catch (StudentOrSubjectDoesntExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping(value = "/subjects/{subjectId}/exams")
    public ResponseEntity<?> addExam( // @RequestBody ExamDto examDto,
            @PathVariable Long subjectId) {
        AddExamResponse addExamResponse = learningNavigatorService.addExamService(subjectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addExamResponse);

    }

    @PostMapping(value = "/students/{studentId}/subjects/{subjectId}/exams/{examId}")
    public ResponseEntity<?> registerExam(@PathVariable Long studentId, @PathVariable Long subjectId,
            @PathVariable String examId) {
        RegisterExamResponse registerExamResponse;
        try {
            registerExamResponse = learningNavigatorService.registerExamService(studentId, subjectId, examId);
            return ResponseEntity.status(HttpStatus.CREATED).body(registerExamResponse);
        } catch (StudentOrSubjectorExamDoesntExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (StudentNotRegisteredInSubjectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }

    }

}
