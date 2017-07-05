#!/bin/bash

set -e

cd src

javac -cp .:../\*:../lib/\* com/arinerron/utils/internetometerbot/InternetometerBotMain.java
java -cp .:../\*:../lib/\* com/arinerron/utils/internetometerbot/InternetometerBotMain $@
