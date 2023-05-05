# MoriaMap

## Links
Releases: https://gaufre.informatique.univ-paris-diderot.fr/tazouev/moriamap/releases

Presentation video:  

UML diagrams: https://gaufre.informatique.univ-paris-diderot.fr/tazouev/moriamap/raw/artifacts/artifacts/UMLs.pdf?inline=false

Javadoc: https://gaufre.informatique.univ-paris-diderot.fr/tazouev/moriamap/raw/artifacts/artifacts/MoriaMap_javadoc.zip

## Important notice
For the calculation of routes including walking sections, a walk speed of 2 km/h is assumed when optimizing for time.
We had to choose this speed because the time transport segments take in the CSV file provided to us is
exaggerated. If we were to walk at 4.5 km/h, Saint-Denis - Université to Basilique de Saint-Denis takes 14 m 18 s.
Compare this to the transport segment on line 13 variant 6 which takes 15 m 34 s.

When optimizing for distance, walking has a drudgery factor of 10 which means that a walk segment is 10 times
longer than its real length, which is calculated using the geographic coordinates between the two points.
Unfortunately this is not enough because the length of transport segments in the
CSV file provided is greatly exaggerated, for example there is a distance of 100 km between Gare de Lyon and
Châtelet (see below). As a result, distance-optimized routes only have walking sections. We are sorry.

`Gare de Lyon;2.372519782814122, 48.8442498880687;Châtelet;2.346411849769497, 48.85955653272677;14 variant 1;26:45;100.92811590723446`

## Features
At the current state the program is able to load the transport network described
in `map_data.csv` (LECT_NET) and the schedule described in `schedule.csv`.  
It is also able to take two stops and display a non-optimized path the user
needs to take on the network to get from one stop to the other (PLAN_0),
display the different transports passages for a given stop (LECT_TIME),
show an optimized in time or distance path to take from given starting and
destination stop (PLAN_1) and be able to have a starting or target point
anywhere on earth and find an optimized path between them sometimes taking the
transport network, or sometimes not (PLAN_2).  
As a separate feature we implemented a way for the optimized route to make it
able to choose walking in the middle of the route if the time it take to wait
for the next transport is taking more time than just walking (PLAN_3).  
The transport network is based off Paris transport network so the stops are
named after real stations like "Lourmel".  

## Instructions

### Running
Extract zip then, if you're using Windows, double-click on the file named
`RUN_WINDOWS.bat`.  
If you're on any linux distribution double-clicking the script `RUN_LINUX.sh`
will allow you to run the program with gnome-terminal or konsole or in your
current terminal if you executed it from one.  
Or you may always open a terminal in the project's directory and just type
`java -jar MoriaMap.jar`.  

### Using
After the project is started, you will be asked to enter one of the following:

    - 1: to display the non-optimized path between two stops (PLAN_0). Then you
         will be asked to enter the names of the starting stop and the target
         stop. Then you will be shown the path you need to take to get from your
         starting stop to the target's.  

    - 2: to display the schedule for a given stop (LECT_TIME). Then you will be
         asked to enter the name of the given stop. Then you will be shown the
         list of passages for the given stop.  

    - 3: to display the optimized path between two stops (PLAN_1). Then you will
         be asked to enter the names of the starting stop and the target stop.
         You will be asked to choose if you want to optimize in time (1) or
         distance (2) and then you need to specify at what time do you want to
         take the path, enter the hours and minutes, or just press ENTER at any
         of them to take your current time. Then you will be shown the path you
         need to take to get from your starting stop to the target's, with the
         times of when you're supposed to arrive and leave a line.  

    - 4: Choose this option if you want to start or finish at any given latitude
         or longitude (PLAN_2). In addition to stop names, you will be able to
         choose your starting and ending points anywhere on earth, you will be
         asked for the latitude and longitude of the point. If the starting or
         target stops position match exactly with any of the stops in the
         transport network then that point will be replaced with the stop
         instead. Just like for PLAN_1 you will then have to choose how do you
         want to optimize your path and at what time do you want to take it. The
         path will then be shown to you with how to make from your starting
         point/stop, to the target point/stop and the moments when you will need
         to walk will be shown with "Walk for <time> from <start> to <end>".  

    - 5: If you want to optimize even more your route, using this option will
         allow for the route to be even more optimized by in some cases choosing
         to walk instead of waiting for a transport when walking is taking less
         effort or time than waiting (PLAN_3).  

    - 6: to exit the program.  

Note: Press CTRL+C to stop program at any moment.

---

## For developers
### Gradle
The project uses Gradle for building.  
- To build the project: `./gradlew assemble`  
- To run unit tests and integration tests: `./gradlew test`  
- To run SonarQube: `./gradlew test sonar` (see below for setup)  
- To generate the Javadoc: `./gradlew javadoc`  
- To run the program: `./gradlew run` or `./gradlew r`  

### GitHub mirror
Our project is mirrored on GitHub (https://github.com/SkyNalix/MoriaMap,
private). Our continuous integration pipeline that builds and tests the project
is on GitHub.

### How to make a release
Merge develop in master, then in the master branch run the script `release.sh`
(run without arguments for usage instructions).

### Setting up SonarQube
- Install Java 17 if it is not installed and make sure `java --version`
  returns 17.
- Download SonarQube Community Edition from
  https://www.sonarsource.com/products/sonarqube/downloads and unzip it into a
  directory which we will call `<SONARQUBE_HOME>` (do not unzip into a directory starting with a digit).
- Execute the following script to start the server:
    - On Linux: `<SONARQUBE_HOME>/bin/linux-x86-64/sonar.sh start`
    - On macOS: `<SONARQUBE_HOME>/bin/macosx-universal-64/sonar.sh start`
    - On Windows: `<SONARQUBE_HOME>/bin/windows-x86-64/StartSonar.bat`
- Login on http://localhost:9000 with username `admin` and password `admin`,
  then change the password to `F9Erj73eUynRrGP`.
- Now you can check out the branch you want to analyze
  (e.g. `git checkout 11-my-feature`) and run `./gradlew test sonar`. You can
  then visit http://localhost:9000 to view the results (SonarQube says the
  branch is `main`, but it actually analyzes the currently checked out branch).
