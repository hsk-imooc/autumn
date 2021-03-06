package org.verlet.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static {
        //默认加载当前模块下的autumn.properties文件
        loadProperties("autumn.properties");
    }

    /**
     * @param fileName 以classpath路径为参考
     */
    private static void loadProperties(String fileName) {
        if (props == null) {
            props = new Properties();
        }
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            logger.error("{} 配置文件读取异常", fileName, e);
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            logger.info("配置文件中没有key为{}的配置", key);
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value.trim();
    }

    /**
     * @Desccription 将文本配置文件信息读取到map中
     */
    public static Map<String, String> readIntoMap(String filePath) {

        try {
            Properties properties = new Properties();
            File file = new File(filePath);
            properties.load(new FileInputStream(file));
            Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();

            Map<String, String> map = new HashMap<>();

            for (Map.Entry<Object, Object> entry : entrySet) {
                String key = entry.getKey().toString().trim();
                if (!key.startsWith("#")) {
                    String value = entry.getValue().toString();
                    map.put(key, value);
                }
            }
            return map;

        } catch (FileNotFoundException e) {
            logger.error("{} 配置文件没有找到", filePath, e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Desccription 将文本配置文件流信息读到map中
     */
    public static Map<String, String> readIntoMap(InputStream in) {

        try {
            Properties properties = new Properties();
            properties.load(in);
            Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();

            Map<String, String> map = new HashMap<>();

            entrySet.forEach(entry -> {
                String key = entry.getKey().toString().trim();
                if (!key.startsWith("#")) {
                    String value = entry.getValue().toString();
                    map.put(key, value);
                }
            });

            return map;

        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param filePath
     * @param key
     * @return String
     * @Desccription 从文本配置文件信息中读取某个属性
     */
    public static String readPropValue(String filePath, String key) {

        try {
            Properties properties = new Properties();
            File file = new File(filePath);
            properties.load(new FileInputStream(file));
            return properties.getProperty(key, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param in
     * @param key
     * @return String
     * @Desccription 从文本配置文件流信息中读取某个属性
     */
    public static String readPropValue(InputStream in, String key) {

        try {
            Properties properties = new Properties();
            properties.load(in);
            return properties.getProperty(key, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param filePath
     * @param key
     * @param value
     * @return boolean
     * @Desccription 向文本配置文件中添加或更新某个属性
     */
    public static boolean writePropValue(String filePath, String key,
                                         String value) {

        try {
            Properties properties = new Properties();
            File file = new File(filePath);
            properties.load(new FileInputStream(file));
            // 写值，key有重复会发生覆盖
            properties.setProperty(key, value);
            properties.store(new FileOutputStream(filePath), "");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
