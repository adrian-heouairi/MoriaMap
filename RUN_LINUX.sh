#!/bin/sh

dir=$(dirname -- "$0")

if [ -t 0 ]; then # If we are in an interactive terminal
    java -jar "$dir"/MoriaMap.jar
else
    if which gnome-terminal; then
        gnome-terminal -- $SHELL -ic 'java -jar "$0"/MoriaMap.jar; $SHELL' "$dir"
    elif which konsole; then
        konsole -e $SHELL -ic 'java -jar "$0"/MoriaMap.jar; $SHELL' "$dir"
    fi
fi