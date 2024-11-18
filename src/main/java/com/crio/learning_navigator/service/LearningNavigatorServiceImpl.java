package com.crio.learning_navigator.service;

import org.springframework.stereotype.Service;

import com.crio.learning_navigator.dto.EnrolledSubjectsDto;
import com.crio.learning_navigator.dto.ExamDto;
import com.crio.learning_navigator.dto.StudentDto;
import com.crio.learning_navigator.dto.StudentEnrollmentDto;
import com.crio.learning_navigator.dto.SubjectDto;
import com.crio.learning_navigator.entity.ExamEntity;
import com.crio.learning_navigator.entity.StudentEntity;
import com.crio.learning_navigator.entity.SubjectEntity;
import com.crio.learning_navigator.exception.StudentNotRegisteredInSubjectException;
import com.crio.learning_navigator.exception.StudentOrSubjectDoesntExistException;
import com.crio.learning_navigator.exception.StudentOrSubjectorExamDoesntExistException;
import com.crio.learning_navigator.exchange.AddExamResponse;
import com.crio.learning_navigator.exchange.AddStudentResponse;
import com.crio.learning_navigator.exchange.AddSubjectResponse;
import com.crio.learning_navigator.exchange.LearningNavigatorResponse;
import com.crio.learning_navigator.exchange.RegisterExamResponse;
import com.crio.learning_navigator.repository.ExamEntityRepository;
import com.crio.learning_navigator.repository.StudentEntityRepository;
import com.crio.learning_navigator.repository.SubjectEntityRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LearningNavigatorServiceImpl implements LearningNavigatorService {

    private ExamEntityRepository examEntityRepository;
    private SubjectEntityRepository subjectEntityRepository;
    private StudentEntityRepository studentEntityRepository;

    public LearningNavigatorServiceImpl(ExamEntityRepository examEntityRepository,
            SubjectEntityRepository subjectEntityRepository, StudentEntityRepository studentEntityRepository) {
        this.examEntityRepository = examEntityRepository;
        this.subjectEntityRepository = subjectEntityRepository;
        this.studentEntityRepository = studentEntityRepository;
    }

    // @Override
    // public LearningNavigatorResponse registerStudentService(ExamDto examDto) {
    // ExamEntity exam = new ExamEntity();
    // exam.setExamId(examDto.getExamId());
    // exam.setSubject(examDto.getSubject());
    // exam.setEnrolledStudents(examDto.getEnrolledStudents());

    // ExamEntity savedExam = examEntityRepository.save(exam);
    // LearningNavigatorResponse learningNavigatorResponse = new
    // LearningNavigatorResponse();
    // learningNavigatorResponse.setExamId(savedExam.getExamId());
    // return learningNavigatorResponse;
    // }

    @Override
    public AddSubjectResponse addSubjectService(SubjectDto subjectDto) {
        SubjectEntity subject = new SubjectEntity();
        subject.setSubjectName(subjectDto.getSubjectName());
        SubjectEntity savedSubject = subjectEntityRepository.save(subject);
        AddSubjectResponse addSubjectResponse = new AddSubjectResponse();
        addSubjectResponse.setSubjectId(savedSubject.getSubjectId());
        return addSubjectResponse;
    }

    @Override
    public AddStudentResponse addStudentService(StudentDto studentDto) {
        StudentEntity student = new StudentEntity();
        student.setStudentName(studentDto.getStudentName());
        StudentEntity savedStudent = studentEntityRepository.save(student);
        AddStudentResponse addStudentResponse = new AddStudentResponse();
        addStudentResponse.setStudentId(savedStudent.getStudentId());
        return addStudentResponse;
    }

    @Override
    public StudentEnrollmentDto enrollSubjectService(Long studentId, Long subjectId)
            throws StudentOrSubjectDoesntExistException {
        if (null == studentId || null == subjectId) {
            throw new StudentOrSubjectDoesntExistException("Student or Subject doesnt exist");
        }
        StudentEntity student = studentEntityRepository.findById(studentId).get();
        SubjectEntity subject = subjectEntityRepository.findById(subjectId).get();
        student.getEnrolledSubjects().add(subject);
        StudentEntity savedStudent = studentEntityRepository.save(student);
        StudentEnrollmentDto studentEnrollmentDto = new StudentEnrollmentDto();
        studentEnrollmentDto.setStudentId(savedStudent.getStudentId());
        studentEnrollmentDto.setStudentName(savedStudent.getStudentName());

        // enrolledSubjectsDto.setSubjectId(subjectId);
        // enrolledSubjectsDto.setSubjectName(subject.getSubjectName());

        List<StudentEntity> studentEntityList = studentEntityRepository.findAllSubjectsByStudentId(studentId);
        // List<SubjectEntity> sub =
        // subjectEntityRepository.findAllSubjectsByStudentId(studentId);
        if (!studentEntityList.isEmpty()) {
            List<SubjectEntity> subjectEntityList = studentEntityList.stream()
                    .flatMap(t -> t.getEnrolledSubjects().stream()) // Flatten each List<SubjectEntity> to a
                                                                    // Stream<SubjectEntity>
                    .collect(Collectors.toList()); // Collect the flattened stream into a List<SubjectEntity>

            System.out.println(studentEntityList);
            System.out.println("subs :" + subjectEntityList);

            for (SubjectEntity sub : subjectEntityList) {
                EnrolledSubjectsDto enrolledSubjectsDto = new EnrolledSubjectsDto();
                enrolledSubjectsDto.setSubjectId(sub.getSubjectId());
                enrolledSubjectsDto.setSubjectName(sub.getSubjectName());
                studentEnrollmentDto.getEnrolledSubjects().add(enrolledSubjectsDto);
            }

            // studentEnrollmentDto.setEnrolledSubjects(savedStudent.getEnrolledSubjects());
            return studentEnrollmentDto;
        }
        return new StudentEnrollmentDto();

    }

    @Override
    public AddExamResponse addExamService(// ExamDto examDto,
            Long subjectId) {
        SubjectEntity returnedSubject = subjectEntityRepository.findById(subjectId).get();
        ExamEntity examEntity = new ExamEntity();
        examEntity.setSubject(returnedSubject);
        ExamEntity savedExamEntity = examEntityRepository.save(examEntity);
        AddExamResponse addExamResponse = new AddExamResponse();
        addExamResponse.setExamId(savedExamEntity.getExamId());
        return addExamResponse;
    }

    @Override
    public RegisterExamResponse registerExamService(Long studentId, Long subjectId, String examId)
            throws StudentOrSubjectorExamDoesntExistException, StudentNotRegisteredInSubjectException {
        // ExamEntity examEntityRetrieved = examEntityRepository.findById(examId).get();
        // System.out.println(examEntityRetrieved);
        // Long subjectForRegisteringExam =
        // examEntityRetrieved.getSubject().getSubjectId();//sub=1
        if (null == studentId || null == subjectId || null == examId) {
            throw new StudentOrSubjectorExamDoesntExistException("Student or Subject doesnt exist or Invalid Exam Id");
        }
        List<StudentEntity> studentEntityList = studentEntityRepository.findAllSubjectsByStudentId(studentId);
        // List<SubjectEntity> sub =
        // subjectEntityRepository.findAllSubjectsByStudentId(studentId);
        if (!studentEntityList.isEmpty()) {
            List<SubjectEntity> subjectEntityList = studentEntityList.stream()
                    .flatMap(t -> t.getEnrolledSubjects().stream()) // Flatten each List<SubjectEntity> to a
                                                                    // Stream<SubjectEntity>
                    .collect(Collectors.toList());// 1
            boolean isRegisteredForSubject = subjectEntityList.stream().anyMatch(t -> t.getSubjectId() == subjectId);
            if (isRegisteredForSubject) {
                ExamEntity examEntity = new ExamEntity();
                SubjectEntity subject = subjectEntityList.stream().filter(t -> t.getSubjectId() == subjectId)
                        .findFirst().get();
                StudentEntity studentEntity = studentEntityList.stream().filter(t -> t.getStudentId() == studentId)
                        .findFirst().get();
                examEntity.setExamId(examId);
                examEntity.setSubject(subject);
                examEntity.setEnrolledStudents(Arrays.asList(studentEntity));
                ExamEntity savedExamEntity = examEntityRepository.save(examEntity);

                RegisterExamResponse registerExamResponse = new RegisterExamResponse();
                registerExamResponse.setExamId(savedExamEntity.getExamId());
                registerExamResponse.setSubject(subject);
                List<Long> enrolledStudentsIds = examEntityRepository.findAllStudentsByExamId(examId);
                registerExamResponse.setEnrolledStudents(enrolledStudentsIds);
                return registerExamResponse;
            } else
                throw new StudentNotRegisteredInSubjectException("Student not registered in the Subject for exam");
        }

        return new RegisterExamResponse();

    }

}
