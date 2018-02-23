JFLAGS = -g
JC = javac
RM = rm -f

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	 ServerConfFile.java\
	 ServerMethod.java\
	 ServerThread.java\
	 Server.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class