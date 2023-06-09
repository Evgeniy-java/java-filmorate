
-- создать нужные таблицы с колонками и pk & fk
CREATE TABLE IF NOT EXISTS MPA
(
	MPA_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS (
	FILM_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION VARCHAR(255) NOT NULL,
	RELEASE_DATE DATE NOT NULL,
	DURATION INTEGER NOT NULL,
	MPA_ID INTEGER,
	CONSTRAINT MPA_ID_FK FOREIGN KEY (MPA_ID) REFERENCES MPA
);

CREATE TABLE IF NOT EXISTS GENRES
(
	GENRE_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	GENRE_NAME VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
	GENRE_ID BIGINT NOT NULL,
	FILM_ID BIGINT NOT NULL,	
	CONSTRAINT GENRE_ID_FK FOREIGN KEY (GENRE_ID) REFERENCES GENRES,
	CONSTRAINT FILMS_ID_FK FOREIGN KEY (FILM_ID) REFERENCES FILMS,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (GENRE_ID,FILM_ID)
);

CREATE TABLE IF NOT EXISTS USERS
(
	USER_ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	EMAIL VARCHAR(255) NOT NULL,
	LOGIN VARCHAR(255) NOT NULL,
	FIRST_NAME VARCHAR(255),
	BIRTHDAY DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM_LIKE
(
	FILM_ID BIGINT NOT NULL,
	USER_ID BIGINT NOT NULL,
	CONSTRAINT FILM_ID_FK FOREIGN KEY (FILM_ID) REFERENCES FILMS,
	CONSTRAINT USER_ID_FK FOREIGN KEY (USER_ID) REFERENCES USERS,	
	CONSTRAINT FILM_LIKE_PK PRIMARY KEY (FILM_ID,USER_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP
(
	USER_ID BIGINT NOT NULL,
	FRIEND_ID BIGINT NOT NULL,
	FRIENDS_STATUS VARCHAR(30),
	CONSTRAINT USER_FROM_FK FOREIGN KEY (USER_ID) REFERENCES USERS,
	CONSTRAINT USER_TO_FK FOREIGN KEY (FRIEND_ID) REFERENCES USERS,
	CONSTRAINT FRIENDSHIP_PK PRIMARY KEY (USER_ID,FRIEND_ID)
);

