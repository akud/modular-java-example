#!/usr/bin/env bash

PROJECTS=$(cat settings.gradle | grep include | awk '{print $2}' | tr -d "'")
BUILD_COMMANDS=$(echo $PROJECTS | sed -e "s/ /:build /g" -e "s/$/:build/")
echo "./gradlew clean $BUILD_COMMANDS"
./gradlew clean $BUILD_COMMANDS
