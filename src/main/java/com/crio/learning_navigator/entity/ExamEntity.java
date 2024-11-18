package com.crio.learning_navigator.entity;

import java.util.List;

import javax.security.auth.Subject;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exam")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamEntity {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String examId;
    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "subject_id", nullable = false, unique = true)
    // private SubjectEntity subject;

    @OneToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectEntity subject;
     @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "exam_student",
        joinColumns = @JoinColumn(name = "exam_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<StudentEntity> enrolledStudents;
    
}


