# **Overview of submission structure:** 
- .idea folder contains all the necessary things for android studio to function correctly
- /app contains the /src folder (source code) and the build.gradle file. build.gradle contains plugins and dependencies for the project
- /app/src contains the androidTest/java/com/example/orientex_v7 folder, the debug folder, the main folder, and the test/java/com/example/orientex_v7 folder.
Both the androidTest/.. and the test/.. folder can be ignored as they are related to unit testing, which was not used.
- The /app/src/debug and associated child folders only contain the icons used within the android operating system
- The /app/src/main is where the majority of the code is. AndroidManifest.xml contains the metadata that android requires for the application to function correctly.
The /res folder within this directory contains all of the necessary xml files for the layout of the app. The majority of these are located in /app/src/main/res/layout.
The /java/com/example/orientex_v7 directory within the aformentioned /app/src/main folder contains all of the source code files for the project.
These are all self-describing source code files with the exception of MainActivity.kt. MainActivity.kt mostly handles the login section of the app and sends necessary
data to other source code files.
- The /Project_Documentation folder contains all of the final report and User Manual for our submission.
