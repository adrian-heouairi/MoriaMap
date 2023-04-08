# MoriaMap

## Features
At the current state the program is able to take two stops and display a path the user need to take on the network to get from one stop to the other.
The stops are named 's1' to 's10', check the image "transport network.jpg" to get a view on the whole network.

## Instructions

### Running
You can directly run the project with `./gradlew run`.  
Or you can build the project into a single JAR file with the 
command `./gradlew build`, the JAR file will be built 
in the directory `build/libs` and can be run with `java -jar build/libs/MoriaMap-<PROJECT_VERSION>.jar`

### Using
After the project is started, you will be asked to enter the
names of the starting stop and the target stop.  
Then you will be shown the path you need to take to get from your starting
stop to the target's, and the program will repeat itself.  
Note: Pressing the Enter key without writing anything allow you to stop the program's execution, or press CTRL+C.

## Setup SonarQube

- Install Java 17 if it is not installed and make sure `java --version` returns 17.
- Download SonarQube Community Edition from https://www.sonarsource.com/products/sonarqube/downloads
and unzip it into a directory which we will call `<SONARQUBE_HOME>`
(do not unzip into a directory starting with a digit).
- Execute the following script to start the server:
  - On Linux: `<SONARQUBE_HOME>/bin/linux-x86-64/sonar.sh start`
  - On macOS: `<SONARQUBE_HOME>/bin/macosx-universal-64/sonar.sh start`
  - On Windows: `<SONARQUBE_HOME>/bin/windows-x86-64/StartSonar.bat`
- Login on http://localhost:9000 with username `admin` and password `admin`, then
change the password to `F9Erj73eUynRrGP`.
- Now you can check out the branch you want to analyze e.g. `git checkout 11-my-feature` and
run `./gradlew test sonar`. You can then visit http://localhost:9000 to view the results
(SonarQube says the branch is `main`, but it actually analyzes the currently checked out branch).
