package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToFriends(long userId, long friendId) {
        String sql = "insert into friendship (user_id, friends_id) values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public Collection<Long> getFriendsIds(long userId) {
        String sql = "select user_id from friendship where friend_id = ?";
        return jdbcTemplate.queryForList(sql, long.class, userId);
    }

    @Override
    public Collection<Long> getCommonFriendsIds(long userId, long friendId) {
        String sql = "select friend_id from friendship where user_id = ? and friend_id in" +
                     "(select friend_id from friendship where user_id = ?)";
        return jdbcTemplate.queryForList(sql, long.class, userId, friendId);
    }

    @Override
    public void deleteFromFriends(long userId, long friendId) {
        String sql = "delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }
}
