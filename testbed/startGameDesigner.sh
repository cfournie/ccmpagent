if [[ !(-e XMLGameDesignerProp.xml) ]]
then 
 cp -f XMLGameDesignerProp_linux.xml XMLGameDesignerProp.xml;
fi
java -XX:NewSize=128m -jar lib/art-game-designer-2.0.1.jar