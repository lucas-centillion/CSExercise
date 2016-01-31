CREATE TABLE role (
  id CHAR(36) NOT NULL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE account (
  id CHAR(36) NOT NULL PRIMARY KEY,
  role_id CHAR(36) NOT NULL,
  fullname TEXT NOT NULL,
  email TEXT NOT NULL,
  password TEXT NOT NULL,
  salt TEXT NOT NULL,
  active BOOLEAN NOT NULL,

  FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE journal (
  id CHAR(36) NOT NULL PRIMARY KEY,
  creator_id CHAR(36) NOT NULL,
  name TEXT NOT NULL,
  active BOOLEAN NOT NULL,

  FOREIGN KEY (creator_id) REFERENCES account(id)
);