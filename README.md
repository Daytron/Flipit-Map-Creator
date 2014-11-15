Build status: &nbsp; ![Travis-Status](https://travis-ci.org/Daytron/Flipit-Map-Creator.svg?branch=master) &nbsp;&nbsp;&nbsp;Build history: [travis-ci.org/Daytron/Flipit-Map-Creator](https://travis-ci.org/Daytron/Flipit-Map-Creator) 

# Flipit Map Creator

Flipit Map Creator is a companion JavaFX application for the upcoming game called Flipit. Allows to create, modify and save Flipit map json files. This application is still in working progress but the barebone functionalities are in placed.

### Features
  - Create and edit tiles on click
  - Create map up to 20 by 20 size
  - Validation on every step, to ensure proper map data modifications

### Screenshot
![ScreenShot](https://raw.githubusercontent.com/Daytron/Flipit-Map-Creator/master/Screenshots/FlipitMapCreatorScreenshot.png?token=AGk1WmfYJhKxIHs7AhfcrrNGFyqGxYXxks5UcIMhwA%3D%3D)

### Dependencies
Flipit Map Creator uses a number of open source projects to work properly:
- [ControlsFX 8.20.8] Use for all dialog windows, Requires Java 8. Note: This library has a new fork that will soon merge with the next JavaFX update in March 2015. For now, the deprecated Dialogs are still in use.
- [Gson 2.3]

### Requirements
- Java 8
- JavaFX

### Version
0.1 Alpha

### Setup Dependencies for Maven
Add the following dependencies to your POM file
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


### Development

Want to contribute? Please do open up an issue. I don't accept pull requests at the moment.

### Todo's

 - Write Tests (Junit and find a great UI Testing library like Robotium for Android)
 - Add Coveralls badge
 - Add dependencies compliance badge
 - Automatic screenshot save image(PNG) in saving map for later use as preview image in the game
 - Further validation on save filename naming convention
 - Players start positions tile marks
 - Create Java doc
 - About Menu
 - Better UI
 - Add Edit menu items (undo and redo maybe in the future?)
 - Add open recent file menu item (no persistence for now)
 - Add keyboard control shortcuts
 - Find bugs
 - Find more bugs

License
----

MIT


[ControlsFX 8.20.8]:http://fxexperience.com/controlsfx/
[Gson 2.3]:https://code.google.com/p/google-gson/
[JavaFX]:http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html

