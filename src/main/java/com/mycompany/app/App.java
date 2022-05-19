package com.mycompany.app;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 */
public class App
{

    private final String message = "Hello World!";

    public App() {}

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        System.out.println(new App().getMessage());
        // Create GroovyClassLoader.
        final GroovyClassLoader classLoader = new GroovyClassLoader();

        // Create a String with Groovy code.
        final StringBuilder groovyScript = new StringBuilder();
        groovyScript.append("class Sample {");
        groovyScript.append("  String sayIt(name) { \"Groovy says: Cool $name!\" }");
        groovyScript.append("}");

        // Load string as Groovy script class.
        Class groovy = classLoader.parseClass(groovyScript.toString());
        GroovyObject groovyObj = (GroovyObject) groovy.newInstance();
        String output = (String) groovyObj.invokeMethod("sayIt", new Object[] { "mrhaki" });
        assert "Groovy says: Cool mrhaki!".equals(output);

        // Load Groovy script file.
        groovy = classLoader.parseClass(new File("src/main/java/com/mycompany/app/GroovyExcelParser.groovy"));
        groovyObj = (GroovyObject) groovy.newInstance();
        groovyObj.invokeMethod("main", new String[]{});
//        assert "Hello mrhaki, from Groovy. Hello mrhaki, from Groovy. ".equals(output);
    }

    private final String getMessage() {
        return message;
    }

}
