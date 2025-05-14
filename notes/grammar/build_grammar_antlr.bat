del *.log
del *.java
del *.class
del *.interp
del *.tokens
del *.log


set LOCATION=antlr-4.13.2.jar
java -jar %LOCATION% -Dlanguage=Java -Xlog -o gen Schiemens.g4

pause