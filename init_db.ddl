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
task_id      INTEGER    NOT NULL,
username     TEXT       NOT NULL,
ip           TEXT,
port         INTEGER,
FOREIGN KEY(task_id) REFERENCES tasks(id));


CREATE TABLE registery (
id           INTEGER    PRIMARY KEY,
ip           TEXT,
port         INTEGER,
project_id   INTEGER    NOT_NULL,
FOREIGN KEY(project_id) REFERENCES projects(project_id));
