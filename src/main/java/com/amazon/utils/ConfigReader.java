package com.amazon.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private final Properties prop = new Properties();

    public ConfigReader() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties NOT FOUND on classpath (place under src/test/resources)");
            }
            prop.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public String getBrowser() {
        return prop.getProperty("browser", "chromium");
    }

    public String getUrl() {
        return prop.getProperty("url");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(prop.getProperty("headless", "false"));
    }

    public int getDefaultTimeoutMs() {
        return Integer.parseInt(prop.getProperty("timeout", "30")) * 1000;
    }
}