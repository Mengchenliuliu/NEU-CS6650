CREATE SCHEMA IF NOT EXISTS SwipeDataBase;
Use SwipeDataBase;

-- create table likes;
DROP TABLE IF EXISTS likes;

CREATE TABLE likes(
swiperId int not null,
numLike int not null DEFAULT 0,
numDislikes int not null DEFAULT 0,
PRIMARY KEY (swiperId),
UNIQUE (swiperId)
);

DROP TABLE IF EXISTS matches;

CREATE TABLE matches(
swiperId int not null,
swipeeId int not null,
num int,
PRIMARY KEY (swiperId, swipeeId),
UNIQUE(swiperId, swipeeId)
);



