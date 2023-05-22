# java-filmorate application
![filmorate DB ER-diagram](images/filmorate_ER-diagram.png)

## ��������� � �����

### movie
���������� ���������� � �������.
#### ������� ������� �� �����:
+ ��������� ���� ```film_id``` - ������������� ������;
+ ```name``` - �������� ������;
+ ```description``` - �������� ������;
+ ```relese_date``` - ��� ������;
+ ```duration``` - ����������������� ������ � �������;
+ ������� ���� ```genre_id``` - ������������� �����;
+ ```reaing``` - ���������� �������, ��������:
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
+ ��������� ���� ```like_id``` - ������������� ������;
+ ��������� ���� ```user_id``` - ������������� ������������;
+ ��������� ���� ```film_id``` - ������������� ������.

### frendlist
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
FROM movie AS m
INNER JOIN genre AS g ON m.genre_id=g.genre_id;
```

+ ������ ��� 20 ������� �� ���������� ������.
```
SELECT m.name, 
       COUNT(l.user_id) AS count_likes
FROM movie AS m
INNER JOIN likes AS l ON m.film_id=l.film_id
GROUP BY m.name
ORDER BY count_likes DESC
LIMIT 20; 
```
