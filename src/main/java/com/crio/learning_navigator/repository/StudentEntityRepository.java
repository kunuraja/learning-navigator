package com.crio.learning_navigator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.crio.learning_navigator.entity.StudentEntity;
import com.crio.learning_navigator.entity.SubjectEntity;

public interface StudentEntityRepository extends CrudRepository<StudentEntity, Long>{
    @Query("select s from StudentEntity s join s.enrolledSubjects se where s.studentId = :studentId")
    List<StudentEntity> findAllSubjectsByStudentId(@Param ("studentId") Long studentId);

    

}
