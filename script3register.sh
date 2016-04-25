#!/bin/bash

if [ $# -ne 3 ]; then
    echo "too few arguments"
else
    java -cp .:lib/* Client -d "$1" -p $2 -g "$3"
fi
