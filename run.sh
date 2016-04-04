#! /bin/bash

javac CollaborativeDashboard/src/Controllers/*.java CollaborativeDashboard/src/gui/*.java CollaborativeDashboard/src/Models/*.java CollaborativeDashboard/src/*.java CollaborativeDashboard/src/DataBase/*.java
cd CollaborativeDashboard/src
java Main
