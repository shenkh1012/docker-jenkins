#!/usr/bin/env bash
JARFILE=Sandy-1.0-SNAPSHOT.jar
file_path=/home/munineyi/workspace/target
file=/home/munineyi/Jenkins-in/Sandy-1.0-SNAPSHOT.jar

# Move the old jar to backup folder
if [ -f "$file" ];then
  mv /home/munineyi/Jenkins-in/Sandy-1.0-SNAPSHOT.jar /home/munineyi/Jenkins-in/backup/Sandy-1.0-SNAPSHOT.jar.`date +%Y%m%d%H%M%S`
fi

# Copy the new jar to target
cp /home/munineyi/workspace/target/Sandy-1.0-SNAPSHOT.jar /home/munineyi/Jenkins-in/

# Delete the old one
cd /home/munineyi/Jenkins-in/backup
ls -lt | awk 'NR>5{print $NF}' | xargs rm -rf
