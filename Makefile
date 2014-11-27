LEXER   = src/parser/Lexer.java
PARSER  = src/parser/Parser.java
JAVASRC = src/*/*.java
BINARY  = bin/main/Main.class

$(BINARY): $(LEXER) $(PARSER) $(JAVASRC)
		@mkdir -p ./bin/
		@javac -cp ./lib/java-cup-11b-runtime.jar -g -sourcepath ./src/ \
				-d ./bin/ ./src/main/*.java

$(PARSER): src/parser/japi.cup
		@java -cp ./lib/ -jar ./lib/java-cup-11b.jar -destdir ./src/parser/ \
				-parser Parser ./src/parser/japi.cup

$(LEXER): src/parser/japi.flex
		@java -jar ./lib/jflex-1.6.0.jar -d ./src/parser/ $1 --nobak \
				./src/parser/japi.flex

clean:
		@rm ./src/parser/*.java
		@rm -r ./bin/
