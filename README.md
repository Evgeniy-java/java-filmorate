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
+ ������� ���� ```genre_id``` - ������������� �����;
+ ```rating``` - ���������� �������, ��������:
	 - PG - ����� ������������� �������� ����� ����� � ����������;
	- PG-13 - ����� �� 13 ��� �������� ����� ����� ������������.

### genre
�������� ���������� � ������ ����.
#### � ������� ������ ����:
+ ��������� ���� ```genre_id``` - ������������� �����;
+ ```name``` - �������� �����.

### likes
�������� ���������� � ������ � ������� ������� ��������� ������������.
#### � ������� ������ ����:
+ ��������� ���� ```film_id``` - ������������� ������;
+ ��������� ���� ```likesbyuser_id``` - ����� �� �������������� ������������.

### friendlist
�������� ���������� � ������� ������ � ������.
#### � ������� ������ ����:
+ ��������� ���� ```user_id``` - ������������� ������������;
+ ```friend_id``` - ������������� ������������ ��� ������;
+ ```friends_status``` - ������ ������������� ���������� � ������.

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
FROM films AS m
INNER JOIN genre AS g ON m.genre_id=g.genre_id;
```

+ ������ ��� 20 ������� �� ���������� ������.
```
SELECT m.name, 
       COUNT(l.user_id) AS count_likes
FROM films AS m
INNER JOIN likes AS l ON m.film_id=l.film_id
GROUP BY m.name
ORDER BY count_likes DESC
LIMIT 20; 
```

+ ������ ��� 10 ������������� � ������������ ����������� ������
```
SELECT u.first_name, 
       COUNT(fL.user_id) AS count_friends
FROM users AS u
INNER JOIN frendlist AS fL ON u.user_id=fL.user_id
WHERE friends_status = 'true'
GROUP BY u.first_name
ORDER BY count_friends DESC
LIMIT 10; 
```


