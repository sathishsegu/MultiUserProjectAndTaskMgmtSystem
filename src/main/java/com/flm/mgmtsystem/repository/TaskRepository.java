package com.flm.mgmtsystem.repository;

import com.flm.mgmtsystem.entity.Project;
import com.flm.mgmtsystem.entity.Task;
import com.flm.mgmtsystem.entity.User;
import com.flm.mgmtsystem.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProject(Project project, Pageable pageable);
    Page<Task> findByAssignedTo(User user, Pageable pageable);
    Page<Task> findByStatus(Status status, Pageable pageable);
    Page<Task> findByStatusAndProjectIn(Status status, Set<Project> projects, Pageable pageable);
    Page<Task> findByStatusAndAssignedTo(Status status, User user, Pageable pageable);
}
