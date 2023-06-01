# java-filmorate application
![filmorate DB ER-diagram](images/filmorate_ER-diagram.png)

## ��������� � �����

### films
���������� ���������� � �������.
#### ������� ������� �� �����:
+ ��������� ���� ```film_id``` - ������������� ������;
+ ```name``` - �������� ������;
+ ```description``` - �������� ������;
+ ```relese_date``` - ��� ������;
+ ```duration``` - ����������������� ������ � �������;
+ ������� ���� ```mpa_id``` - ������������� ��������;

### mpa
�������� ���������� � �������� ���������� ������������ (MPA)
#### � ������� ������ ����:
+ ��������� ���� ```mpa_id``` - ������������� ��������;
+ ```rating``` - ���������� �������;
+ ```description``` - �������� �������� ��������:
	 - PG - ����� ������������� �������� ����� ����� � ����������;
	- PG-13 - ����� �� 13 ��� �������� ����� ����� ������������.

### film_genre
�������� ����������� ��������������� ����� � ������, �������� ��������� ���� ����� ��������� films � genre
#### � ������� ������ ����:
+ ��������� ���� ```genre_id``` - ������������� �����;
+ ������� ���� ```film_id``` - ������������� ������.

### genre
�������� ���������� � ������ ����.
#### � ������� ������ ����:
+ ��������� ���� ```genre_id``` - ������������� �����;
+ ```name``` - �������� �����.

### film_likes
�������� ���������� � ������ � ������� ������� ��������� ������������.
#### � ������� ������ ����:
+ ��������� ���� ```film_id``` - ������������� ������;
+ ������� ���� ```user_id``` - ������������� ������������.

### friendship
�������� ���������� � ������� ������ � ������.
#### � ������� ������ ����:
+ ��������� ���� ```user_id``` - ������������� ������������;
+ ��������� ���� ```friend_id``` - ������������� ������������ ��� ������;
+ ```status``` - ������ ������.

### users
�������� ������ � �������������.
#### ������� �������� ����:
+ ��������� ���� ```user_id``` - ������������� ������������;
+ ```email``` - ����������� �����;
+ ```login``` - ��� ����������� ������������;
+ ```first_name``` - ��� ������������;
+ ```birthday``` - ���� �������� ������������.


##������� �������� ����������:
+ �������� ���� genre � ������� movie � ������� ���������� � �������.
```
SELECT *
FROM films AS f
INNER JOIN genre AS g ON g.genre_id=fG.genre_id;
INNER JOIN film_genre AS fG ON f.id=fG.film_id;
```

+ ������ ��� 20 ������� �� ���������� ������.
```
SELECT m.name, 
       COUNT(fL.user_id) AS count_likes
FROM films AS m
INNER JOIN film_like AS fL ON m.film_id=fL.film_id
GROUP BY m.name
ORDER BY count_likes DESC
LIMIT 20; 
```

+ ������ ��� 10 ������������� � ������������ ����������� ������
```
SELECT u.first_name, 
       COUNT(f.friend_id) AS count_friends
FROM users AS u
INNER JOIN friendship AS f ON u.user_id=f.user_id
WHERE status = 'CONFIRMED'
GROUP BY u.first_name
ORDER BY count_friends DESC
LIMIT 10; 
```


