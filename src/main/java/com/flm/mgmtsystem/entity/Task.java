package com.flm.mgmtsystem.entity;

import com.flm.mgmtsystem.entity.enums.Priority;
import com.flm.mgmtsystem.entity.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String taskTitle;

    @Column(nullable = false)
    private String taskDescription;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user_id", nullable = false)
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, updatable = false)
    private LocalDateTime taskCreatedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onTaskCreate() {
        this.taskCreatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = Status.TODO;
    }

    @PreUpdate
    protected void onTaskUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Version
    @Column(nullable = false)
    private Long version;
}
