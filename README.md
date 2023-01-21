# java-filmorate
Template repository for Filmorate project.

[Схема БД](https://dbdiagram.io/d/63b9c2627d39e42284e98530)

Пояснения:
<br>
Таблица User - хранит данные о пользователях
<br>
Таблица Friends - хранит статусы дружеских связей, всего 2 строки
<br>
Таблица Films - хранит данные о фильмах
<br>
Таблица Ratings - хранит данные о рейтинге Ассоциации кинокомпаний 
<br>
Таблица Genres - хранит данные о жанрах
<br>
Таблица Film_Genres - хранит данные о жанрах, которые относятся к фильмам. У одного фильма может быть много жанров
<br>
Таблица Film_Likes - хранит данные о лайках, которые поставили пользователи
<br>
<br>
1. Чтобы получить список друзей пользователя с id 1 необходимо выполнить следующий запрос:
<br>
SELECT USERS.* 
<br>
FROM friends
<br>
INNER JOIN USERS ON FRIENDS.FRIEND_ID = USERS.USER_ID
<br>
WHERE FRIENDS.USER_ID=1;
<br>
<br>

2. Чтобы получить общий друзей пользователей необходимо выполнить следующий запрос:
<br>
SELECT *
<br>
FROM USERS
<br>
WHERE user_id IN (
<br>
(SELECT f1.friend_id
<br>
FROM (SELECT user_id, friend_id
<br>
FROM friends
<br>
WHERE user_id = ?) AS f1
<br>
INNER JOIN <br>
(SELECT user_id, friend_id
<br>
FROM friends
<br>
WHERE user_id = ?) as f3
<br>
ON f1.friend_id = f3.friend_id))
<br>
<br>
