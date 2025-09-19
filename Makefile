# CS 356:Assignment 1

OUTPUT = Cipher.class
SRC = Cipher.java

all: $(OUTPUT)

$(OUTPUT): $(SRC)
	javac $(SRC)

run: $(OUTPUT)
	java Cipher $(ARGS)

clean:
	rm -f *.class

.PHONY: all run clean
