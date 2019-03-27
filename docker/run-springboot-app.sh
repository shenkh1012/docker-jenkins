#!/usr/bin/env bash
BASE_PATH=/work/project
SOURCE_PATH=/root/.jenkins/workspace
SERVER_NAME=docker-test
CID=$(docker ps | grep "$SERVER_NAME" | awk '{print $1}')
IID=$(docker images | grep "$SERVER_NAME" | awk '{print $3}')
DATE='date +%Y%m%d%H%M'

# Move jar to working folder
function deliver(){
    echo "Deliver jar ${SOURCE_PATH}/${SERVER_NAME}/target/${SERVER_NAME}.jar 迁移至 ${BASE_PATH}..."
        cp ${SOURCE_PATH}/${SERVER_NAME}/target/${SERVER_NAME}.jar ${BASE_PATH}
    echo "Deliver finish"
}

function backup(){
	if [[ -f "$BASE_PATH/${SERVER_NAME}.jar" ]]; then
    	echo "Backup ${SERVER_NAME}.jar..."
        	cp ${BASE_PATH}/${SERVER_NAME}.jar ${BASE_PATH}/backup/${SERVER_NAME}-${DATE}.jar
        echo "Backup $SERVER_NAME.jar finish"
    else
    	echo "${BASE_PATH}/${SERVER_NAME}.jar doesn't exit"
    fi
}

#Build image
function build(){
	if [[ -n "${IID}" ]]; then
		echo "Image $SERVER_NAME already exists，IID=$IID"
	else
		echo "Image $SERVER_NAME doesn't exit，start to build image..."
		cd $[BASE_PATH]
		docker build -t ${SERVER_NAME} .
	fi
}

# Main
function run(){
	backup
	deliver
	build
	if [[ -n "$CID" ]]; then
		echo "Container ${SERVER_NAME} exits，CID=${CID}, restart docker container..."
		docker restart ${SERVER_NAME}
		echo "$[SERVER_NAME] started."
	else
		echo "Container ${SERVER_NAME} doesn't exit，create container..."
		docker run --name ${SERVER_NAME} -v ${BASE_PATH}:${BASE_PATH} -d -p 8081:8080 ${SERVER_NAME}
		echo "$SERVER_NAME created."
	fi
}

# Kik off.
run
