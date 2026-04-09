package com.amazon.utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestDataReader {

    private final JSONObject testData;

    public TestDataReader() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testdata.json")) {
            if (is == null) {
                throw new RuntimeException("testdata.json NOT FOUND on classpath (place under src/test/resources)");
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[4096];
            int n;
            while ((n = is.read(chunk)) != -1) {
                buffer.write(chunk, 0, n);
            }
            String json = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
            testData = new JSONObject(new JSONTokener(json));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load testdata.json", e);
        }
    }

    public JSONObject getData(String testName) {
        return testData.getJSONObject(testName);
    }

    public String getData(String suiteKey, String fieldKey) {
        return getData(suiteKey).getString(fieldKey);
    }
}