CC=javac
CFLAGS = -cp .:lib/*
SRC = $(wildcard *.java)
CLASS = $(SRC:.java=.class)
EXE = $(basename $(SRC))

all: $(EXE) 

%:%.java
	$(CC) $(CFLAGS) $<

clean:
	rm -f $(CLASS)


