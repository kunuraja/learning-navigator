package com.crio.learning_navigator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.HashSet;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import com.crio.learning_navigator.dto.EnrolledSubjectsDto;
import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.dto.StudentEnrollmentDto;
import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.entity.SubjectEntity;
import com.crio.learning_navigator.exchange.AddStudentResponse;
import com.crio.learning_navigator.exchange.AddSubjectResponse;
import com.crio.learning_navigator.exchange.RegisterExamResponse;
import com.crio.learning_navigator.service.LearningNavigatorService;
import com.crio.learning_navigator.service.LearningNavigatorServiceImpl;

import static com.crio.learning_navigator.constatnts.testConstants.ADD_STUDENT_API_URI;
import static com.crio.learning_navigator.constatnts.testConstants.ADD_STUDENT_ID_URI;
import static com.crio.learning_navigator.constatnts.testConstants.ADD_SUBJECT_API_URI;
import static com.crio.learning_navigator.constatnts.testConstants.ADD_SUBJECT_ID_URI;
import static com.crio.learning_navigator.constatnts.testConstants.EXAM_ID;
import static com.crio.learning_navigator.constatnts.testConstants.REGISTER_EXAM_API_URI;
import static com.crio.learning_navigator.constatnts.testConstants.REGISTER_EXAM_ID_URI;
import static com.crio.learning_navigator.constatnts.testConstants.STUDENT_ID;
import static com.crio.learning_navigator.constatnts.testConstants.SUBJECT_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@SpringBootTest
@AutoConfigureMockMvc
public class LearningNavigatorControllerTest {

    private MockMvc mvc;

    @MockBean
    private LearningNavigatorServiceImpl learningNavigatorServiceMock;


    @InjectMocks
    private LearningNavigatorController learningNavigatorController;

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


    @BeforeEach
    public void setup() {
    //objectMapper = new ObjectMapper();

    MockitoAnnotations.initMocks(this);

    mvc = MockMvcBuilders.standaloneSetup(learningNavigatorController).build();

    subjectDto = new SubjectDto("sub1") ; 
    subject = new SubjectEntity(subjectId, subjectName, null);
    addSubjectResponse = new AddSubjectResponse(subjectId);
    studentDto = new StudentDto(studentName) ;   
    addStudentResponse = new AddStudentResponse(studentId); 
    enrolledSubjectsDto = new EnrolledSubjectsDto(subjectId, subjectName) ;
    studentEnrollmentDto = new StudentEnrollmentDto(STUDENT_ID, studentName, List.of(enrolledSubjectsDto)); 
    registerExamResponse = new RegisterExamResponse(EXAM_ID,subject,List.of(STUDENT_ID)) ;

    }
    @Test
    void testAddExam() {

       

    }

    @Test
    void testAddStudent() throws Exception {
        String requestBody = "{\r\n" + //
                        "    \"studentName\" : \"Raj\"\r\n" + //
                        "}";

        URI uri = UriComponentsBuilder
        .fromPath(ADD_STUDENT_API_URI)
        .build()
        .toUri();
  
        assertEquals(ADD_STUDENT_API_URI, uri.toString());
  
        when(learningNavigatorServiceMock.addStudentService(studentDto)).thenReturn(addStudentResponse);
  
        MvcResult result = mvc.perform(post(uri.toString())
                                .content(requestBody)
                .contentType("application/json; charset=utf-8")
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

                              System.out.println("result :" +result.getResponse().getContentAsString());
                              String response = "{\"studentId\":1}";
  
          verify(learningNavigatorServiceMock, times(1)).addStudentService(studentDto);
          assertEquals(response, result.getResponse().getContentAsString());

    }

    @Test
    void testAddSubject() throws Exception {

        String requestBody = "{\r\n" + //
                        "    \"subjectName\" : \"sub1\"\r\n" + //
                        "}";

        URI uri = UriComponentsBuilder
        .fromPath(ADD_SUBJECT_API_URI)
        .build()
        .toUri();
  
        assertEquals(ADD_SUBJECT_API_URI, uri.toString());
  
        when(learningNavigatorServiceMock.addSubjectService(subjectDto)).thenReturn(addSubjectResponse);
  
        MvcResult result = mvc.perform(post(uri.toString())
                                .content(requestBody)
                .contentType("application/json; charset=utf-8")
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

                              System.out.println("result :" +result.getResponse().getContentAsString());
                              String response = "{\"subjectId\":1}";
  
          verify(learningNavigatorServiceMock, times(1)).addSubjectService(subjectDto);
          assertEquals(response, result.getResponse().getContentAsString());

    }

    @Test
    void testEnrollSubject() throws Exception {

        URI uri = UriComponentsBuilder
                .fromPath(ADD_STUDENT_API_URI+ ADD_STUDENT_ID_URI+ADD_SUBJECT_API_URI+ADD_SUBJECT_ID_URI)
                .buildAndExpand(STUDENT_ID, SUBJECT_ID)
                .toUri();

        assertEquals(ADD_STUDENT_API_URI+"/1"+ADD_SUBJECT_API_URI+"/1", uri.toString());

        when(learningNavigatorServiceMock.enrollSubjectService(STUDENT_ID,SUBJECT_ID)).thenReturn(studentEnrollmentDto);

        MvcResult result = mvc.perform(put(uri.toString()))
                .andExpect(status().isCreated())
                .andReturn();

        System.out.println("result :" + result.getResponse().getContentAsString());
       // String response = "{\"subjectId\":1}";

        verify(learningNavigatorServiceMock, times(1)).enrollSubjectService(STUDENT_ID, SUBJECT_ID);
      //  assertEquals(response, result.getResponse().getContentAsString());

    }

    @Test
    void testRegisterExam() throws Exception {

        URI uri = UriComponentsBuilder
                .fromPath(ADD_STUDENT_API_URI + ADD_STUDENT_ID_URI + ADD_SUBJECT_API_URI + ADD_SUBJECT_ID_URI
                +REGISTER_EXAM_API_URI + REGISTER_EXAM_ID_URI)
                .buildAndExpand(STUDENT_ID, SUBJECT_ID, EXAM_ID)
                .toUri();

        assertEquals(ADD_STUDENT_API_URI + "/1" + ADD_SUBJECT_API_URI + "/1"+REGISTER_EXAM_API_URI+"/ex-1-sub1" , uri.toString());

        when(learningNavigatorServiceMock.registerExamService(studentId, subjectId, EXAM_ID))
                .thenReturn(registerExamResponse);

        MvcResult result = mvc.perform(post(uri.toString()))
                .andExpect(status().isCreated())
                .andReturn();

        System.out.println("result :" + result.getResponse().getContentAsString());
        String response = "{\"examId\":\"ex-1-sub1\",\"subject\":{\"subjectId\":1,\"subjectName\":\"sub1\",\"enrolledStudents\":null},\"enrolledStudents\":[1]}";

        verify(learningNavigatorServiceMock, times(1)).registerExamService(STUDENT_ID, SUBJECT_ID, EXAM_ID);
         assertEquals(response, result.getResponse().getContentAsString());

    }
}
