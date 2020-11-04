#!/bin/bash
#remove old
rm /tmp/.X99-lock #needed when docker container is restarted
Xvfb :99 -screen 0 640x480x8 -nolisten tcp &
java -jar ac-1.0-jar-with-dependencies.jar -m $MASTERIP -id $ID