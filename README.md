# Range Selector
This is a utility for generating a GUI and related controllers/providers to obtain a selectable range. The initial target
is for selecting a frequency range in a simple and intuitive way.

Initially, this will focus on providing a Swing based GUI for selecting a range of values, but the goal is to 
offer both Swing and JavaFX-based solutions eventually.

This is the ultra-simple proof-of-concept view:
![Simple Range Selection](doc/simple-range-selection-panel.gif)

Here is a basic histogram-based range selection view:
![Basic Histogram](doc/basic-histogram-with-legend.gif)

## Requirements
This project requires:
* JDK 11 ([AdoptOpenJDK](https://adoptopenjdk.net) is an excellent, free, OpenJDK option.)
* [JFormDesigner](https://www.formdev.com) for working with the Swing UI `.jfd` files

### Other Key Libraries
This project leverages a few key libraries:
* Spring Boot (dependency injection framework and application runner)
* Apache Commons Lang3 (primarily for the `Range` class)

## Building

To build this project:
```shell script
./gradlew clean build
```

## Usage

### Command Line Runner Example

To run the basic sample from the command line, execute:
```shell script
./gradlew runSample
```

### As a Library in Your Project

This is intended to be pulled into another project as a library, as such, it is designed to be very portable.

TODO

Add the dependency:
```groovy
implementation 'com.mikeoertli:range-selector'
```

Inject the `RangeSelectorProvider` and request a `RangeSelectorController`:
```java
public class MyApplication {

    @Inject
    private RangeSelectorProvider rangeSelectorProvider;
    
    public void doStuff()
    {
        RangeSelectorController controller = rangeSelectorProvider.createRangeSelectorController();
        
        RangeSelectorPanel panel = controller.getPanel();

        // ...
    }

}
```


## Author

[Mike Oertli](https://github.com/mikeoertli)


Questions, comments, and pull requests are welcome!