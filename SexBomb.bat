@echo off
cd %~p0
if exist SexBomb.out del SexBomb.out
if exist SexBomb.err del SexBomb.err
java 1>>SexBomb.out 2>>SexBomb.err -jar SexBomb.jar
