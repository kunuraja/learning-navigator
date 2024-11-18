package com.crio.learning_navigator.service;

import static com.crio.learning_navigator.constatnts.testConstants.EXAM_ID;
import static com.crio.learning_navigator.constatnts.testConstants.STUDENT_ID;
import static com.crio.learning_navigator.constatnts.testConstants.SUBJECT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crio.learning_navigator.controller.LearningNavigatorController;
import com.crio.learning_navigator.dto.EnrolledSubjectsDto;
import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.dto.StudentEnrollmentDto;
import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.entity.ExamEntity;
import com.crio.learning_navigator.entity.StudentEntity;
import com.crio.learning_navigator.entity.SubjectEntity;
import com.crio.learning_navigator.exception.StudentNotRegisteredInSubjectException;
import com.crio.learning_navigator.exception.StudentOrSubjectDoesntExistException;
import com.crio.learning_navigator.exception.StudentOrSubjectorExamDoesntExistException;
import com.crio.learning_navigator.exchange.AddStudentResponse;
import com.crio.learning_navigator.exchange.AddSubjectResponse;
import com.crio.learning_navigator.exchange.RegisterExamResponse;
import com.crio.learning_navigator.repository.ExamEntityRepository;
import com.crio.learning_navigator.repository.StudentEntityRepository;
import com.crio.learning_navigator.repository.SubjectEntityRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class LearningNavigatorServiceImplTest {

    private MockMvc mvc;

    @MockBean
    private ExamEntityRepository examEntityRepositoryMock;

    @MockBean
    private StudentEntityRepository studentEntityRepositoryMock;

    @MockBean
    private SubjectEntityRepository subjectEntityRepositoryMock;

    @InjectMocks
    private LearningNavigatorServiceImpl learningNavigatorServiceImplMock;

    private SubjectDto subjectDto;
    private AddSubjectResponse addSubjectResponse;
    private Long subjectId = 1L;
    private StudentDto studentDto;
    private String studentName = "Raj";
    private AddStudentResponse addStudentResponse;
    private Long studentId = 1L;
    private StudentEnrollmentDto studentEnrollmentDto;
    private EnrolledSubjectsDto enrolledSubjectsDto;
    private String subjectName = "sub1";
    private RegisterExamResponse registerExamResponse;
    private SubjectEntity subject;
    private StudentEntity studentEntity;

    @BeforeEach
    public void setup() {
        // objectMapper = new ObjectMapper();

        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(learningNavigatorServiceImplMock).build();

        subjectDto = new SubjectDto("sub1");
        subject = new SubjectEntity(subjectId, subjectName, null);
        studentEntity = new StudentEntity(studentId, studentName, new ArrayList<SubjectEntity>(), null);
        addSubjectResponse = new AddSubjectResponse(subjectId);
        studentDto = new StudentDto(studentName);
        addStudentResponse = new AddStudentResponse(studentId);
        enrolledSubjectsDto = new EnrolledSubjectsDto(subjectId, subjectName);
        studentEnrollmentDto = new StudentEnrollmentDto(STUDENT_ID, studentName, List.of(enrolledSubjectsDto));
        registerExamResponse = new RegisterExamResponse(EXAM_ID, subject, List.of(STUDENT_ID));

    }

    @Test
    void testAddExamService() {

    }

    @Test
    void testAddStudentService() {
        StudentEntity studentToSave = new StudentEntity();
        studentToSave.setStudentName("Raj");
        when(studentEntityRepositoryMock.save(studentToSave)).thenReturn(studentEntity);
        addStudentResponse = learningNavigatorServiceImplMock.addStudentService(studentDto);
        assertEquals(1, addStudentResponse.getStudentId());
        verify(studentEntityRepositoryMock, times(1)).save(studentToSave);

    }

    @Test
    void testAddSubjectService() {
        SubjectEntity subjectToSave = new SubjectEntity();
        subjectToSave.setSubjectName("sub1");
        when(subjectEntityRepositoryMock.save(subjectToSave)).thenReturn(subject);
        addSubjectResponse = learningNavigatorServiceImplMock.addSubjectService(subjectDto);
        // assertEquals("sub1", subject.getSubjectName());
        assertEquals(1, addSubjectResponse.getSubjectId());
        verify(subjectEntityRepositoryMock, times(1)).save(subjectToSave);

    }

    @Test
    void testEnrollSubjectService() throws StudentOrSubjectDoesntExistException {

        when(subjectEntityRepositoryMock.findById(SUBJECT_ID)).thenReturn(Optional.of(subject));
        when(studentEntityRepositoryMock.findById(STUDENT_ID)).thenReturn(Optional.of(studentEntity));
        studentEntity.getEnrolledSubjects().add(subject);
        when(studentEntityRepositoryMock.save(studentEntity)).thenReturn(studentEntity);
        StudentEnrollmentDto studentEnrollmentDto = new StudentEnrollmentDto();
        studentEnrollmentDto.setStudentId(studentEntity.getStudentId());
        studentEnrollmentDto.setStudentName(studentEntity.getStudentName());
        List<StudentEntity> studentEntityList = List.of(studentEntity);
        when(studentEntityRepositoryMock.findAllSubjectsByStudentId(STUDENT_ID)).thenReturn(List.of(studentEntity));
        List<SubjectEntity> subjectEntityList = studentEntityList.stream()
                .flatMap(t -> t.getEnrolledSubjects().stream()) // Flatten each List<SubjectEntity> to a
                                                                // Stream<SubjectEntity>
                .collect(Collectors.toList());
        for (SubjectEntity sub : subjectEntityList) {
            EnrolledSubjectsDto enrolledSubjectsDto = new EnrolledSubjectsDto();
            enrolledSubjectsDto.setSubjectId(sub.getSubjectId());
            enrolledSubjectsDto.setSubjectName(sub.getSubjectName());
            studentEnrollmentDto.getEnrolledSubjects().add(enrolledSubjectsDto);
        }

        StudentEnrollmentDto studentEnrollmentDtoTest = learningNavigatorServiceImplMock.enrollSubjectService(studentId,
                subjectId);

        assertEquals(1, studentEnrollmentDtoTest.getStudentId());
        verify(subjectEntityRepositoryMock, times(1)).findById(SUBJECT_ID);

    }

    @Test
    void testRegisterExamService()
            throws StudentOrSubjectorExamDoesntExistException, StudentNotRegisteredInSubjectException {
        List<StudentEntity> studentEntityList = List.of(studentEntity);
        when(studentEntityRepositoryMock.findAllSubjectsByStudentId(STUDENT_ID)).thenReturn(List.of(studentEntity));

        List<SubjectEntity> subjectEntityList = studentEntityList.stream()
                .flatMap(t -> t.getEnrolledSubjects().stream()) // Flatten each List<SubjectEntity> to a
                                                                // Stream<SubjectEntity>
                .collect(Collectors.toList());
        boolean isRegisteredForSubject = subjectEntityList.stream().anyMatch(t -> t.getSubjectId() == subjectId);
        if (isRegisteredForSubject) {

            ExamEntity examEntity = new ExamEntity();
            SubjectEntity subject = subjectEntityList.stream().filter(t -> t.getSubjectId() == subjectId)
                    .findFirst().get();
            StudentEntity studentEntity = studentEntityList.stream().filter(t -> t.getStudentId() == studentId)
                    .findFirst().get();
            examEntity.setExamId(EXAM_ID);
            examEntity.setSubject(subject);
            examEntity.getEnrolledStudents().add(studentEntity);
            when(examEntityRepositoryMock.save(examEntity)).thenReturn(examEntity);
            ExamEntity savedExamEntity = examEntityRepositoryMock.save(examEntity);

            RegisterExamResponse registerExamResponse = new RegisterExamResponse();
            registerExamResponse.setExamId(savedExamEntity.getExamId());
            registerExamResponse.setSubject(subject);
            when(examEntityRepositoryMock.findAllStudentsByExamId(EXAM_ID)).thenReturn(List.of(STUDENT_ID));
            List<Long> enrolledStudentsIds = examEntityRepositoryMock.findAllStudentsByExamId(EXAM_ID);
            registerExamResponse.setEnrolledStudents(enrolledStudentsIds);

            RegisterExamResponse registerExamResponseTest = learningNavigatorServiceImplMock
                    .registerExamService(studentId, subjectId, EXAM_ID);

            assertEquals(registerExamResponse.getExamId(), registerExamResponseTest.getExamId());
            verify(examEntityRepositoryMock, times(1)).findAllStudentsByExamId(EXAM_ID);

        }

    }
}
