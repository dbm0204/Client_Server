CC=javac
CFLAGS = -cp .:lib/*
SRC = $(wildcard *.java)
CLASS = $(SRC:.java=.class)
EXE = $(basename $(SRC))

all: Parser $(EXE) 


Parser: Parser.jj
	java -cp lib/javacc.jar javacc $<
	$(CC) $(CFLAGS) Parser.java

%:%.java
	$(CC) $(CFLAGS) $<

clean:
	rm -f $(CLASS)


