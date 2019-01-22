#!/bin/sh -e
export CLASSPATH="$CLASSPATH:"'/opt/pi4j/lib/*'
export JAVA_TOOL_OPTIONS="-Dpi4j.linking=dynamic"
java -jar KofeMashine-all-1.0-SNAPSHOT.jar
