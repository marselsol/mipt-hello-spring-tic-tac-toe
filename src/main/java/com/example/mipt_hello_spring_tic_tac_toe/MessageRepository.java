package com.example.mipt_hello_spring_tic_tac_toe;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Message> MESSAGE_ROW_MAPPER = (rs, rowNum) -> {
        Message m = new Message(rs.getString("content"));
        m.setId(rs.getLong("id"));
        return m;
    };

    public Message save(Message message) {
        if (message.getId() == null) {
            final String sql = "INSERT INTO messages(content) VALUES (?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, message.getContent());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                message.setId(key.longValue());
            }
        } else {
            jdbcTemplate.update("UPDATE messages SET content = ? WHERE id = ?", message.getContent(), message.getId());
        }
        return message;
    }

    public Message findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT id, content FROM messages WHERE id = ?", MESSAGE_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Iterable<Message> findAll() {
        List<Message> list = jdbcTemplate.query("SELECT id, content FROM messages ORDER BY id", MESSAGE_ROW_MAPPER);
        return list;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM messages WHERE id = ?", id);
    }

    public void clear() {
        jdbcTemplate.update("DELETE FROM messages");
    }
}
