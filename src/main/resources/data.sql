DROP TABLE IF EXISTS user_accounts;

CREATE TABLE user_accounts (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_id VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

INSERT INTO user_accounts (id,user_id, password) VALUES
  (1,'admin', 'password');

DROP TABLE IF EXISTS access_tokens;

CREATE TABLE access_tokens (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  access_token VARCHAR(250) NOT NULL,
  publish_at DATE NOT NULL
);

DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  department VARCHAR(250) NOT NULL
);
