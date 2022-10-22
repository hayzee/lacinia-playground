CREATE TABLE starwars_character
(
id   INT  NOT NULL PRIMARY KEY,
name text NOT NULL
);


INSERT INTO starwars_character VALUES (1, 'Obi-Wan Kenobi');

INSERT INTO starwars_character VALUES (2, 'Darth Vader');

INSERT INTO starwars_character VALUES (3, 'Chewbacca');

INSERT INTO starwars_character VALUES (4, 'Princess Leia');

INSERT INTO starwars_character VALUES (5, 'R2-D2');

INSERT INTO starwars_character VALUES (6, 'C3-PO');

CREATE TABLE character_episode
(
character_id INT  NOT NULL,
episode      varchar(200) NOT NULL
);

INSERT INTO character_episode
SELECT sc.id,
       CASE x
         when 1 then 'NEWHOPE'
         when 2 then 'EMPIRE'
         when 3 then 'JEDI'
       END episode
FROM starwars_character sc
     JOIN (SELECT x FROM generate_series(1,3) x)
     ;

DELETE FROM character_episode WHERE episode = 'NEWHOPE' AND character_id IN (1, 3, 5);

DELETE FROM character_episode WHERE episode = 'EMPIRE' AND character_id IN (2, 4, 6);

DELETE FROM character_episode WHERE episode = 'JEDI' AND character_id IN (5, 2, 1);
