# java-filmorate
Template repository for Filmorate project.

[Схема БД](https://dbdiagram.io/d/63b9c2627d39e42284e98530)

Пояснения:
<br>
Таблица User - хранит данные о пользователях
<br>
Таблица FriendStatus - хранит статусы дружеских связей, всего 2 строки
<br>
Таблица Friendlist - хранит списки друзей, и статусы этих дружеских связей (подтверждена или нет)
<br>
Таблица Film - хранит данные о фильмах
<br>
Таблица MPA - хранит данные о рейтинге Ассоциации кинокомпаний 
<br>
Таблица genre - хранит данные о жанрах
<br>
Таблица genres - хранит данные о жанрах, которые относятся к фильмам. У одного фильма может быть много жанров
<br>
Таблица Likes - хранит данные о лайках, которые поставили пользователи
<br>
<br>
1. Чтобы получить список друзей пользователя с id 1 необходимо выполнить следующий запрос:
<br>
SELECT u.login,
<br>
  fs.name
  <br>
FROM Friendlist AS fl
<br>
LEFT JOIN FriendStatus AS fs ON fl.id_status=fs.id
<br>
LEFT JOIN User AS u ON fl.id_friend=u.id
<br>
WHERE fs.id=1;
<br>
<br>
<br>

2. Чтобы получить общий друзей пользователей с id 1 и 2 необходимо выполнить следующий запрос:
<br>
SELECT login<br>
FROM User<br>
WHERE id IN (SELECT id_friend<br>
FROM Friendlist<br>
WHERE id=1<br>
AND id_friend IN (SELECT id_friend<br>
FROM Friendlist<br>
WHERE id=2)<br>
)<br>
<br>
