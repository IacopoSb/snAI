#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <black|white> <timeout> <server>"
    exit 1
fi

JAR_DIR="/home/tablut/tablut/Tablut/jars"
JAR_FILE="snai.jar"

if [ ! -f "$JAR_DIR/$JAR_FILE" ]; then
    echo "Error: $JAR_DIR/$JAR_FILE not found."
    exit 1
fi

java -jar "$JAR_DIR/$JAR_FILE" "$1" "$2" "$3"
