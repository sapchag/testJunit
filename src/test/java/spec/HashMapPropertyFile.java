package spec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class HashMapPropertyFile {

    static final String path = "src/main/resources/";
    static final Logger logger = LoggerFactory.getLogger(HashMapPropertyFile.class);

    public static void write(String name, HashMap hmap) {
        Properties properties = new Properties();
        hmap.forEach((k, v) -> properties.setProperty(k.toString(), v.toString()));

        OutputStream output = null;

        try {
            output = new FileOutputStream(path + name + ".properties");
            properties.store(output, null);

        } catch (IOException io) {
            logger.info(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }
    }

    public static HashMap load(String propertiesName) {
        Properties properties = new Properties();
        InputStream input = null;
        HashMap<String, String> hMap = new HashMap<String, String>();

        try {

            String filename = path + propertiesName + ".properties";
            input = new FileInputStream(filename);

            if (input == null) {
                logger.info("Sorry, unable to find " + filename);
                return hMap;
            }

            properties.load(input);

            for (final String name: properties.stringPropertyNames())
                hMap.put(name, properties.getProperty(name));

        } catch (IOException ex) {
            logger.info(ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
        }
        return hMap;
    }
}
