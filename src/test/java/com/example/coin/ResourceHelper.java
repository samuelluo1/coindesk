package com.example.coin;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * some utils to help transfer json file into JAVA class
 */
public class ResourceHelper {

    private ResourceHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * get the resource as String
     *
     * @see <a href="https://developer.aliyun.com/article/796000">cychen1981</a>
     * @param clazz class name
     * @param name name of the resource
     * @return String
     */
    public static <T> String getResourceAsString(Class<T> clazz, String name) {
        try (InputStream is = clazz.getResourceAsStream(name)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to transform the file (%s)", name), e);
        }
    }
}
