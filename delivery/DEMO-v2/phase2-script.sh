#!/bin/bash



echo "==================================================="
echo "Extension: card DRAW_THREE -- Example 1"
echo "==================================================="
echo -e "(2 Players) \n\n"
java -cp "project-v2.jar:project-v1.jar" uno.v2.MainV2 -p2 card DRAW_THREE DEMO-v2/input/deck1-draw3.txt DEMO-v2/input/script1-draw3.txt 2 3 > DEMO-v2/output/out1-draw3.txt


echo "==================================================="
echo "Extension: card DRAW_THREE -- Example 2"
echo "==================================================="
echo -e "(3 Players) \n\n"
java -cp "project-v2.jar:project-v1.jar" uno.v2.MainV2 -p2 card DRAW_THREE DEMO-v2/input/deck2-draw3.txt DEMO-v2/input/script2-draw3.txt 3 3 > DEMO-v2/output/out2-draw3.txt


echo "==================================================="
echo "Extension: card DRAW_THREE -- Example 3"
echo "==================================================="
echo -e "(3 Players) \n\n"
java -cp "project-v2.jar:project-v1.jar" uno.v2.MainV2 -p2 card DRAW_THREE DEMO-v2/input/deck3-draw3.txt DEMO-v2/input/script3-draw3.txt 3 3 > DEMO-v2/output/out3-draw3.txt


echo "==================================================="
echo "Extension: card DRAW_THREE -- Example 4"
echo "==================================================="
echo -e "(4 Players) \n\n"
java -cp "project-v2.jar:project-v1.jar" uno.v2.MainV2 -p2 card DRAW_THREE DEMO-v2/input/deck4-draw3.txt DEMO-v2/input/script4-draw3.txt 4 3 > DEMO-v2/output/out4-draw3.txt


echo "==================================================="
echo "Extension: card DRAW_THREE -- Example 5"
echo "==================================================="
echo -e "(4 Players) \n\n"
java -cp "project-v2.jar:project-v1.jar" uno.v2.MainV2 -p2 card DRAW_THREE DEMO-v2/input/deck5-draw3.txt DEMO-v2/input/script5-draw3.txt 4 3 > DEMO-v2/output/out5-draw3.txt









echo "================================================================================================="
echo "==================================================="
echo "Extension: card SpeedRuleset -- Example 1"
echo "==================================================="
echo -e "(2 Players) \n\n"
java -cp "project-v2.jar:project-v1.jar" uno.v2.MainV2 -p2 ruleset SpeedRuleset DEMO-v2/input/deck1-speed.txt DEMO-v2/input/script1-speed.txt 2 3> DEMO-v2/output/out1-speed.txt


echo "================================================================================================="
echo "==================================================="
echo "Extension: card SpeedRuleset -- Example 2"
echo "==================================================="
echo -e "(3 Players) \n\n"
java -cp "project-v2.jar:project-v1.jar" uno.v2.MainV2 -p2 ruleset SpeedRuleset DEMO-v2/input/deck1-speed.txt DEMO-v2/input/script1-speed.txt 3 3> DEMO-v2/output/out1-speed.txt
