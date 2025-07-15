package com.todo.backend.repository;

import com.todo.backend.model.ListEntity;
import com.todo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListRepository extends JpaRepository<ListEntity, Long> {
    List<ListEntity> findByUser(User user);
}
