USE spotify_history;

CREATE TABLE user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL
);
