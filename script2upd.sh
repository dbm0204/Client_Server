#!/bin/bash

msg="GET_PROJECT;Quiz"

echo -e "Since all the dates are in the past, " \
        "the tasks that have been assigned will " \
        "return as done and the others will be waiting" \
        "\n\n"

echo -e $msg | java -cp .:lib/* Client -a "localhost" -p 2356 -u

