# MoriaMap

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
