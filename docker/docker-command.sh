#!/usr/bin/env bash

#https://www.cnblogs.com/sammyliu/p/5932996.html

# Clean up docker volumes
docker volume ls -qf dangling=true
docker volume rm $(docker volume ls -qf dangling=true)
docker volume ls
