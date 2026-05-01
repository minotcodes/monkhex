#!/bin/sh

DIR="$(cd "$(dirname "$0")" && pwd)"
JAVA_EXE="${JAVA_HOME:+$JAVA_HOME/bin/java}"
if [ -z "$JAVA_EXE" ]; then
  JAVA_EXE="java"
fi
"$JAVA_EXE" -classpath "$DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
