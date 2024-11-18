package com.crio.learning_navigator.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subject")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long subjectId;
    private String subjectName;
    
    // @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @JoinTable(
    //     name = "subject_student",
    //     joinColumns = @JoinColumn(name = "subject_id"),
    //     inverseJoinColumns = @JoinColumn(name = "student_id")
    // )
    // @ManyToMany(mappedBy = "registeredStudents", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private transient List<StudentEntity> enrolledStudents = new ArrayList<>();

    // @OneToOne(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private ExamEntity exam;
    
}

