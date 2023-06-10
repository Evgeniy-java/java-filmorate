package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
@Primary
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUserById(long id) {
        String sql = "Select user_id, email, login, first_name, birthday from users where user_id = ?";

        if (userExists(id)) {
            return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        } else {
            log.debug("некорректный id пользователя{}", id);
            throw new NotFoundException(String.format("некорректный id %s пользователя", id));
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User createUser(User user) {

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("first_name", user.getName());
        values.put("birthday", user.getBirthday());

        long userId = (long) insert.executeAndReturnKey(values);
        user.setId(userId);

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userExists(user.getId())) {
            jdbcTemplate.queryForObject("select user_id from users where user_id = ?", Long.class, user.getId());
        } else {
            log.debug("некорректный id пользователя{}", user.getId());
            throw new NotFoundException(String.format("некорректный id %s пользователя", user.getId()));
        }

        String sql = "update users set email = ?, login = ?, first_name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        return user;
    }

    @Override
    public boolean userExists(long id) {
        String sql = "select count (user_id) from users where user_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);

        if (count != 0) {
            return true;
        } else return false;
    }

    public Collection<User> getUserFriends(Set<Long> friendsId) {
        String sql = String.join(",", Collections.nCopies(friendsId.size(), "?"));

        return jdbcTemplate.query(String.format("select * from users where user_id in (%s)", sql),
                new UserMapper(),
                friendsId.toArray());
    }

    private static class UserMapper implements RowMapper<User> {
        User user = new User();

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("first_name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setFriends(null);

            return user;
        }
    }
}