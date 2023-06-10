--INSERT INTO MPA (NAME) VALUES 
--('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

--INSERT INTO GENRES (GENRE_NAME) 
--VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) 
VALUES ('film1','description1','2021-01-01','60','1'),('film2','description2','2022-02-02','120','2');

INSERT INTO USERS (EMAIL, LOGIN, FIRST_NAME, BIRTHDAY) 
VALUES ('test1@mail.ru','login1','firstName1','2001-01-01'),('test2@mail.ru','login2','firstName2','2002-02-02');

--INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) 
--VALUES ('1','2');

--INSERT INTO FILM_LIKE (FILM_ID, USER_ID) 
--VALUE ('2','1');