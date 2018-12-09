#!/bin/bash
#this is a fix for kinect errors
#this should probably run on startup on the robot

./fix.o &

sleep 10

killall fix.o
