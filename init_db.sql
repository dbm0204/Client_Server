CREATE TABLE tasks (
id            INTEGER     PRIMARY KEY,
project       TEXT        NOT NULL,
task          TEXT        NOT NULL,
start         DATETIME    NOT NULL,
end           DATETIME    NOT NULL,
UNIQUE(project, task));


CREATE TABLE users (
id            INTEGER     PRIMARY KEY,
username      TEXT        NOT NULL,
ip            TEXT        NOT NULL,
port          INT         NOT NULL,
task_id       INT         NOT NULL,
FOREIGN KEY(task_id) REFERENCES tasks(id));
