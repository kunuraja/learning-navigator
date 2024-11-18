package com.crio.learning_navigator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.crio.learning_navigator.entity.StudentEntity;
import com.crio.learning_navigator.entity.SubjectEntity;

public interface SubjectEntityRepository extends CrudRepository<SubjectEntity, Long>{

    //  @Query("select s from SubjectEntity s join s.enrolledStudents se where se.studentId = :studentId")
    // List<SubjectEntity> findAllSubjectsByStudentId(@Param ("studentId") Long studentId);

}