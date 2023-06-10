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

    @Override
    public Collection<Long> getPopularFilmsIds(long count) {
        String sql = "select f.film_id " +
                     "from films as f " +
                     "left join film_like as fL on f.film_id=fL.film_id " +
                     "group by f.film_id " +
                     "order by count(fL.user_id) desc " +
                     "limit ?";

        return jdbcTemplate.queryForList(sql, Long.class, count);
    }
}