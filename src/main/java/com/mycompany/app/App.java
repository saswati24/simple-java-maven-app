package com.mycompany.app;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;

public class App
{

    private final String message = "Hello World!";

    public App() {}

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
          System.out.println("Command-line arguments:");

          for (String arg : args) {

                  System.out.println(arg);

          }
        // Create GroovyClassLoader.
        final GroovyClassLoader classLoader = new GroovyClassLoader();

        // Load Groovy script file.
        Class groovy = classLoader.parseClass(new File("src/main/java/com/mycompany/app/GroovyExcelParser.groovy"));
        GroovyObject groovyObj = (GroovyObject) groovy.newInstance();
        groovyObj.invokeMethod("main", args);
//        assert "Hello mrhaki, from Groovy. Hello mrhaki, from Groovy. ".equals(output);
    }

}
