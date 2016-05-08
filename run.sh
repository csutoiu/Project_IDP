#! /bin/bash

javac -cp \* CollaborativeDashboard/src/Controllers/*.java CollaborativeDashboard/src/gui/*.java CollaborativeDashboard/src/Models/*.java CollaborativeDashboard/src/*.java CollaborativeDashboard/src/DataBase/*.java CollaborativeDashboard/src/NIO/*.java
cd CollaborativeDashboard/src

if [ $# -lt 1 ]
then 
 	echo -e "Please provide at least one username!\nUse: <./run.sh> <alice> <bob> ..."
	exit 0
fi

echo "This users will be added in data base with email <username>@idp.com and password \"pass\"."
echo "If an user is the N argument then he will be connected in Nth instance of application."

cmd=""
for var in "$@"
do
	cmd="$cmd java -cp .:../../* Main 1"
	cmd="$cmd "$var""
	cmd="$cmd & sleep 1000 & "
done
cmd="$cmd java -cp .:../../* Main 0"

echo $cmd

eval $cmd


