package ru.yandex.practicum.filmorate.daoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.LikesDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/data-test.sql"})
public class LikeDaoTest {
    private final LikesDao likesDao;

    @Test
    void addLike() {
        likesDao.addLike(2, 2);

        assertEquals(2,likesDao.getLikedUsersId(2).size());
    }

    @Test
    void deleteLike() {
        likesDao.deleteLike(2, 1);

        assertEquals(0,likesDao.getLikedUsersId(1).size());
    }

    @Test
    void getLikedUsersIds() {
        assertThat(likesDao.getLikedUsersId(2)
                .contains(2L));
    }

    @Test
    void getPopularFilmsIds() {
        List<Long> popularFilmsIds = new ArrayList<>(likesDao.getPopularFilmsIds(1));
        List<Long> expectedIds = Collections.singletonList(2L);

        assertThat(popularFilmsIds).isEqualTo(expectedIds);
    }
}