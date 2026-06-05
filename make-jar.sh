#!/bin/bash

find /home/bossman/projects/Java/game/ -type f -name "*.java" -exec javac {} + 

# Empacota a Fase 1 (código fonte e classes compiladas das pastas base)
jar cmf manifest-v1.txt project-v1.jar uno/Main.java uno/Main.class uno/api uno/engine uno/io uno/model

# Empacota a Fase 2 (apenas a pasta de extensão v2)
jar cmf manifest-v2.txt project-v2.jar uno/v2project-v2.jar



#  ~/projects/Java/game/source1  main !3 ?4 ───────────────────────────────────────────────────────────────────────────────────────────────────────────────╮
# ❯ javadoc -d . $(find . -name "*.java")                                                                                                                   ─╯
# ~/projects/Java/game/source1  main !3 ?4 ────────────────────────────────────────────────────────────────────────────────────────────────────── INT ✘ ─╮
# ❯ mkdir docs                                                                                                                                              ─╯
#  ~/projects/Java/game/source1  main !3 ?4 ───────────────────────────────────────────────────────────────────────────────────────────────────────────────╮
# ❯ javadoc -d docs $(find . -name "*.java")  