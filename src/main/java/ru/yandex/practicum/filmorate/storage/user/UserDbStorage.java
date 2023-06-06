package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Qualifier
    private final FriendsDao friendsDao;

    @Override
    public User getUserById(long id) {
        String sql = "Select user_id, email, login, first_name, birthday from users where user_id = ?";

        if (userExists(id)) {
            return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        } else {
            log.debug("некорректный id пользователя{}", id);
            throw new IncorrectParameterException(String.format("некорректный id %s пользователя", id));
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("first_name", user.getName());
        values.put("birthday", user.getBirthday());

        long userId = (long) simpleJdbcInsert.executeAndReturnKey(values);
        user.setId(userId);

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userExists(user.getId())) {
            jdbcTemplate.queryForObject("select user_id from users where users_id = ?", Long.class, user.getId());
        } else {
            log.debug("некорректный id пользователя{}", user.getId());
            throw new IncorrectParameterException(String.format("некорректный id %s пользователя", user.getId()));
        }

        String sql = "update users set email = ?, login = ?, first_name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        return user;
    }

    @Override
    public boolean userExists(long id) {
        String sql = "select count (user_id) from users where user_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);

        return (count == 1) ? true : false;
    }

    @Override
    public Collection<User> getUserFriends(Set<Long> friendsId) {
        String sql = String.join(",", Collections.nCopies(friendsId.size(), "?"));

        return jdbcTemplate.query(String.format("select * from users where user_id in (%s)", sql),
                new UserMapper(),
                friendsId.toArray());
    }

    private class UserMapper implements RowMapper<User> {
        User user = new User();

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.getFriends().addAll(friendsDao.getFriendsIds(user.getId()));

            return user;
        }
    }
}