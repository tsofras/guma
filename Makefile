VERSION=1.6
SOURCE=guma

guma: guma/Main.java arithmetic core
	javac ${SOURCE}/*.java

arithmetic:
	javac ${SOURCE}/arithmetic/*.java

core: 
	javac ${SOURCE}/core/*.java

run: guma guma/Main.class
	java guma.Main

jar: 
	jar cvfe ./guma-${VERSION}.jar guma.Main guma/*

clean:
	rm -fr guma/*.class && rm -fr guma/*/*.class && rm -fr guma/*/*/*.class

javadoc:
	mkdir -p javadoc/guma && javadoc guma/* && mv ./*.html javadoc && mv guma/*.html javadoc/guma && mv *.css javadoc

build: guma jar clean

tar: build run.bat run.sh Licence.txt CHANGES.TXT
	tar -cvzf guma-${VERSION}.tar.gz guma-${VERSION}.jar run.sh run.bat Licence.txt CHANGES.TXT 
