package ru.yandex.practicum.filmorate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.FriendsDao;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema-test.sql","/data-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FriendDaoTest {
    private final FriendsDao friendsDao;

    @Test
    void addToFriends() {
        friendsDao.addToFriends(2, 1);
        assertThat(friendsDao.getUserFriendsById(1)).hasSize(1);
    }

    @Test
    void deleteFromFriends() {
        friendsDao.deleteFriend(1, 2);
        assertThat(friendsDao.getUserFriendsById(1)).hasSize(0);
    }

    @Test
    void getFriendsIds() {
        assertThat(friendsDao.getUserFriendsById(1)).hasSize(1);
    }

    @Test
    void getCommonFriendsIds() {
        assertThat(friendsDao.getCommonFriends(2, 1)).hasSize(0);
    }
}
