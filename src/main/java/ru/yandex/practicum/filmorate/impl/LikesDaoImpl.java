package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikesDao;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class LikesDaoImpl implements LikesDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "insert into film_like (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "delete from film_like where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Collection<Long> getLikedUsersId(long filmId) {
        String sql = "select user_id from film_like where film_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, filmId);
    }
}