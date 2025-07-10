LIB_DIR = 'lib'
BIN_DIR = 'bin'
SRC_DIR = 'src'
JAR_NAME= 'genetic_algo.jar'

.PHONY: ALL

compile: 
	javac -cp "$(LIB_DIR)/*" -d $(BIN_DIR) $(SRC_DIR)/*.java

test-run: compile
	java -cp "bin:lib\*:lib\commons-lang3-3.12.0.jar" Main

package:
	jar cfm $(JAR_NAME) META-INF/MANIFEST.MF -C $(BIN_DIR) .  

run-jar:
	java -jar $(JAR_NAME)
