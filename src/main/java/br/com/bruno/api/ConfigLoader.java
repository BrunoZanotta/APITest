package br.com.bruno.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties = new Properties();

        static {
            try {
                FileInputStream file = new FileInputStream("src/main/resources/environment.properties");
                properties.load(file);
            } catch (IOException e ) {
                throw new RuntimeException("Failed to load the configuration file.");
            }
        }

        public static String getProperty(String key) {
            return properties.getProperty(key);
        }
}
