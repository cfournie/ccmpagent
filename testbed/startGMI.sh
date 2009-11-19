#!/bin/sh

export ART_LIB=./lib

export CLASSPATH=$ART_CVS/bin:$ART_LIB/art-2.0.1.jar:$ART_LIB/commons-beanutils.jar:$ART_LIB/commons-digester.jar:$ART_LIB/jcommon.jar:$ART_LIB/jfreechart.jar:$ART_LIB/commons-collections-3.2.jar:$ART_LIB/commons-logging.jar:$ART_LIB/jdom.jar:$ART_LIB/mysql-connector-java-3.1.8-bin.jar

java -cp $CLASSPATH -XX:NewSize=128m testbed.gui.gmi.GameMonitorInterface config/game-config.xml