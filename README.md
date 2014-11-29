# ![app icon] &nbsp; Flipit Map Creator   
[![Build Status](https://travis-ci.org/Daytron/Flipit-Map-Creator.svg?branch=master)](https://travis-ci.org/Daytron/Flipit-Map-Creator) [![Dependency Status](https://www.versioneye.com/user/projects/546a1a2395082530790000bf/badge.svg?style=flat)](https://www.versioneye.com/user/projects/546a1a2395082530790000bf) [![License](http://img.shields.io/:license-mit-blue.svg)](https://raw.githubusercontent.com/Daytron/Flipit-Map-Creator/master/LICENSES/LICENSE)

Flipit Map Creator is a companion JavaFX application for the upcoming game called Flipit. Allows to create, modify and save Flipit map json files. This application is still in working progress but the barebone functionalities are in placed.

### Features
  - Create and replace tiles on click
  - Can create map with size up to 20 by 20 tiles.
  - Automatically saves a preview image file of the map.
  - Display a log of events to monitor action history

### Screenshot
![ScreenShot](https://raw.githubusercontent.com/Daytron/Flipit-Map-Creator/master/Screenshots/FlipitMapCreatorScreenshot.png)

### Dependencies
Flipit Map Creator uses a number of open source projects to work properly:
- [ControlsFX 8.20.8] Use for all dialog windows, Requires Java 8. Note: This library has a new fork that will soon merge with the JDK version 8u40 update in March 2015 as the official dialog for JDK. For now, the deprecated Dialogs are still in use. See announcement [here](http://fxexperience.com/2014/09/announcing-controlsfx-8-20-7/). For the license look [here](https://raw.githubusercontent.com/Daytron/Flipit-Map-Creator/master/LICENSES/CONTROLSFX_LICENSE.txt).
- [Gson 2.3] For converting json files to Java objects and vice versa. See license [here](https://raw.githubusercontent.com/Daytron/Flipit-Map-Creator/master/LICENSES/GSON_LICENSE.txt).
- [TestFX 3.1.2] A simple GUI test framework for JavaFX application.
- [JUnit 4.11] De facto standard unit test framework for Java application.

### Requirements
- Java 8
- JavaFX

### Version
0.1 Alpha

### Setup Dependencies for Maven
Add the following dependencies to your POM file:
```
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.3</version>
</dependency>
<dependency>
    <groupId>org.controlsfx</groupId>
    <artifactId>controlsfx</artifactId>
    <version>8.20.8</version>
</dependency>
```
### For Test Dependencies for Maven
Currently using prerelease version (SNAPSHOT) of TestFX. Please add the following to your pom file.

```
<repositories>
    <repository>
        <id>maven-central-repo</id>
        <url>http://repo1.maven.org/maven2</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>sonatype-snapshots-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```
Then on your dependencies add the following:
```
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-core</artifactId>
    <version>4.0.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
</dependency>
```
### Development

Want to contribute? Please do open up an issue for any bug reports and feedback. I'm not accepting any pull requests at the moment.

### Build
You can build the project through Maven by using the following command:
```
mvn clean install
```

### Run
You can run the application jar with:
```
java -jar Flipit-Map-Creator-<Version number>.jar
```
Change `<Version number>` with the current version number.
### Javadoc
[Javadoc source]

### Roadmap to version 1.0 release
See Milestone and Issues sections for more information and updates.

 - Write Tests for JUnit and TestFX (For TestFX, wait for version 3.2 DeathThread bug fix)
 - Add and achieve high test coverage (Cobertura) and include Coveralls badge
 - ~~Automatic screenshot save image(PNG) in saving map for later use as preview image in the game~~
 - ~~Further validation on save filename naming convention~~
 - ~~Players start positions tile marks~~
 - ~~Create Javadoc~~
 - ~~About Menu~~
 - ~~Add open recent file menu item (no persistence for now)~~
 - ~~Add keyboard control shortcuts~~
 - Find bugs
 - Find more bugs

License
----

MIT


[ControlsFX 8.20.8]:http://fxexperience.com/controlsfx/
[Gson 2.3]:https://code.google.com/p/google-gson/
[JavaFX]:http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html
[TestFX 3.1.2]:https://github.com/TestFX/TestFX
[JUnit 4.11]:http://junit.org/
[Javadoc source]:https://daytron.github.io/Flipit-Map-Creator/apidocs/
[app icon]:https://raw.githubusercontent.com/Daytron/Flipit-Map-Creator/master/src/main/resources/images/icon42x42.png
