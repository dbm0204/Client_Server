#!/bin/bash
datafile="database.db"
initdb="init_db.ddl"
java -cp .:lib/* Server -p $1 -d $datafile -ddl $initdb
