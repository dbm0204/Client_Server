CREATE TABLE projects (
id     INTEGER     PRIMARY KEY,
name   TEXT        NOT NULL UNIQUE);

CREATE TABLE tasks (
id             INTEGER      PRIMARY KEY,
project_id     TEXT         NOT NULL,
task           TEXT         NOT NULL,
start          DATETIME     NOT NULL,
end            DATETIME     NOT NULL,
FOREIGN KEY(project_id) REFERENCES projects(id));

CREATE TABLE users (
id           INTEGER    PRIMARY KEY,
username     TEXT       NOT NULL,
ip           TEXT       NOT NULL,
port         INTEGER    NOT NULL,
task_id      INTEGER    NOT NULL,
FOREIGN KEY(task_id) REFERENCES tasks(id));
