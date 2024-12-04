

javac -cp %cd% *.java

java -cp %cd% MainAutomato %1 %2 > logs.txt

if "%2" == "c" gcc traducao.c && ./a.out
if "%2" == "j" javac -cp %cd% traducao.java && java -cp %cd% traducao

