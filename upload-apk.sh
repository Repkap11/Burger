#!/bin/bash
if [ "$#" -ne 1 ]; then
	echo "usage: Please spesify the version of the app to upload."
	exit
fi
APP_VERSION=$1
LOCAL_FILE=./app/build/outputs/apk/burger-debug-$APP_VERSION.apk
REMOTE_USERNAME=paul
REMOTE_SERVER=repkap11.com
REMOTE_FILE=/home/paul/website/burger/Burger.apk
REMOTE_TARGET=$REMOTE_USERNAME@$REMOTE_SERVER:$REMOTE_FILE
echo "Copying $LOCAL_FILE to $REMOTE_TARGET"
rsync --update $LOCAL_FILE $REMOTE_TARGET

