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
        // Create GroovyClassLoader.
        final GroovyClassLoader classLoader = new GroovyClassLoader();

        // Load Groovy script file.
        groovy = classLoader.parseClass(new File("src/main/java/com/mycompany/app/GroovyExcelParser.groovy"));
        groovyObj = (GroovyObject) groovy.newInstance();
        groovyObj.invokeMethod("main", new String[]{});
//        assert "Hello mrhaki, from Groovy. Hello mrhaki, from Groovy. ".equals(output);
    }

}
