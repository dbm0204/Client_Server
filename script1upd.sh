#!/bin/bash

msg="PROJECT_DEFINITION:MidTerm;TASKS:2;Buy paper;2016-03-12:18h30m00s001Z;2016-03-15:18h30m00s001Z;Write exam;2016-03-15:18h30m00s001Z;2016-03-15:18h30m00s001Z;\nTAKE;USER:Johny;PROJECT:MidTerm;Buy paper\nGET_PROJECTS\nGET_PROJECT;MidTerm"

echo -e $msg | nc localhost -u 2356

