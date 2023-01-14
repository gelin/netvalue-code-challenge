export JAVA_HOME = /usr/lib/jvm/java-1.17.0-openjdk-amd64

.PHONY: build
build:
	./gradlew build

.PHONY: clean
clean:
	./gradlew clean

.PHONY: run
run:
	"$$JAVA_HOME/bin/java" -jar build/libs/codechallenge-*[!plain].jar
