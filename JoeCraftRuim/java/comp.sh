clear
filename=$1
ext="${filename##*.}"
name="${filename%.*}"
#(j)abaixo a linha antiga de compilação
#javac -encoding UTF8 $1

#adicionei "-Xlint:-removal" abaixo p parar de aparecer warnings de "AudioClip deprecated"
javac -Xlint:-removal -encoding UTF8 $1

#Note: Recompile with -Xlint:removal for details.
#Note: ./J.java uses unchecked or unsafe operations.
#Note: Recompile with -Xlint:unchecked for details.

#(j)compilando e exibindo os warnings: (basicamente sobre AudioClip, q tá "deprecated")
#javac -Xlint:removal -Xlint:unchecked -encoding UTF8 $1

#(j)compilação corriqueira:
#javac -Xlint:-removal -encoding UTF8 $1
java $name
#java $name $2
echo 
echo ===================================================================
echo 
