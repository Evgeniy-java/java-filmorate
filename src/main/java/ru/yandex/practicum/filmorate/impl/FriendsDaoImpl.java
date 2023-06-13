package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToFriends(long userId, long friendId) {
        String sql = "insert into friendship (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
        log.debug("Добавлен пользователь с id {} в друзья пользователю с id {}", friendId, userId);
    }

    @Override
    public Collection<Long> getUserFriendsById(long userId) {
        String sql = "select friend_id from friendship where user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlQuery = "select * from users " +
                "where user_id in (select friend_id from friendship where user_id = ?) " +
                "and user_id in (select friend_id from friendship where user_id = ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        List<User> commonFriends = new ArrayList<>();
        while (rs.next()) {
            commonFriends.add(new User(rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("first_name"),
                    Objects.requireNonNull(rs.getDate("birthday")).toLocalDate()));
        }
        return commonFriends.stream().distinct().collect(Collectors.toList());
        }


    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
        log.debug("Удален пользователь с id {} из друзей пользователя с id {}", friendId, userId);
    }
}