CREATE DATABASE spotify_history;

USE spotify_history;

CREATE TABLE tracks (
  id VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  artist VARCHAR(255) NOT NULL,
  artist_id VARCHAR(255) NOT NULL,
  last_played DATETIME NOT NULL,
  INDEX artist_id (artist_id),
  CONSTRAINT tracks_pk PRIMARY KEY (id, last_played)
);
