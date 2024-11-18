package com.crio.learning_navigator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.crio.learning_navigator.entity.ExamEntity;
import com.crio.learning_navigator.entity.StudentEntity;

public interface ExamEntityRepository extends CrudRepository<ExamEntity, String> {
    
    @Query("select st.studentId from ExamEntity e join e.enrolledStudents st where e.examId = :examId")
    List<Long> findAllStudentsByExamId(@Param ("examId") String examId);
}
