package com.example.mipt_hello_spring_tic_tac_toe;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m JOIN FETCH m.author ORDER BY m.createdAt DESC")
    List<Message> findLatestWithAuthors(Pageable pageable);

    List<Message> findTop30ByOrderByCreatedAtDesc();
}
