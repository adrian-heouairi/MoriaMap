# MoriaMap

## Features
At the current state the program is able to load the transport network described in map_data.csv (LECT_NET) and the schedule described in schedule.csv.  
It is also able to take two stops and display a non-optimized path the user needs to take on the network to get from one stop to the other (PLAN_0),  
display the different transports passages for a given stop (LECT_TIME),  
show an optimized in time or distance path to take from given starting and destination stop (PLAN_1)  
and be able to have a starting or target point anywhere on earth and find an optimized path between  
them sometimes taking the transport network, or sometimes not (PLAN_2).  
As a separate feature we implemented a way for the optimized route to make it able to choose walking in the middle of the route  
if the time it take to wait for the next transport is taking more time than just walking (PLAN_3).  
The transport network is based off Paris transport network so the stops are named after real stations like "Lourmel".  

## Instructions

### Running
Extract zip then if you're using Windows, you should double-click on the file named `RUN_WINDOWS.bat`.  
If you're on any linux distribution double-clicking the script `RUN_LINUX.sh` will allow you to run the program with gnome-terminal or konsole  
or in your current terminal if you executed it from one  
Or you may always open a terminal in the project's directory and just type `java -jar MoriaMap.jar`  

### Using
After the project is started, you will be asked to enter:

    - 1 if you want to display the non-optimized path between two stops (PLAN_0).
      Then you will be asked to enter the names of the starting stop and the target stop.
      Then you will be shown the path you need to take to get from your starting
      stop to the target's.

    - 2 if you want to display the schedule for a given stop (LECT_TIME).
      Then you will be asked to enter the name of the given stop.
      Then you will be shown the list of passages for the given stop.

    - 3 if you want to display the optimized path between two stops (PLAN_1).
      Then you will be asked to enter the names of the starting stop and the target stop.
      You will be asked to choose if you want to optimize in time (1) or distance (2)
      and then you need to specify at what time do you want to take the path, enter the hours and minutes,
      or just press ENTER at any of them to take your current time.
      Then you will be shown the path you need to take to get from your starting
      stop to the target's, with the times of when you're supposed to arrive and leave a line.

    - 4 Choose this option if you want to start or finish at any given latitude or longitude (PLAN_2).
      In addition to stop names, you will be able to choose your starting and ending points anywhere on earth,
      you will be asked for the latitude and longitude of the point.
      If the starting or target stops position match exactly with any of the stops in the transport network
      then that point will be replaced with the stop instead.
      Just like for PLAN_1 you will then have to choose how do you want to optimize your path
      and at what time do you want to take it.
      The path will then be shown to you with how to make from your starting point/stop, to the target point/stop
      and the moments when you will need to walk will be shown with "Walk for <time> from <start> to <end>".

    - 5 If you want to optimize even more your route, using this option will allow for the route to be even more
      optimized by in some cases choosing to walk instead of waiting for a transport when walking is taking less
      effort or time than waiting (PLAN_3).

    - 6 if you want to exit the program.

Note: Pressing CTRL+C at any moment allows you to stop the program's execution.
