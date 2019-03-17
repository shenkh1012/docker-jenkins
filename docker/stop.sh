#!/usr/bin/env bash
#!stop.sh
#!/bin/bash
JARFILE=Sandy-1.0-SNAPSHOT.jar

cd /home/munineyi/Jenkins-in
echo "Stopping SpringBoot Application for Sandy"
ls

# Get pid for the app
pid=`ps -ef | grep $JARFILE | grep -v grep | awk '{print $2}'`

# Kill the process
if [ -n "$pid" ];then
  kill -9 $pid
fi

