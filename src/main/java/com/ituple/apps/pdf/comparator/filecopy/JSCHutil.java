package com.ituple.apps.pdf.comparator.filecopy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JSCHutil {

	static JSCHLogger jschLogger = new JSCHLogger();

	public static Properties getProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("C:\\DUMP\\config.properties"));
		return properties;
	}

	public static OutputStream getOutputStream() throws FileNotFoundException, IOException {
		OutputStream output = new FileOutputStream("C:\\DUMP\\config.properties");
		return output;
	}

	public static Map getMap() throws FileNotFoundException, IOException {
		Properties properties = JSCHutil.getProperties();
		Map<String, String> propertiesMap = new HashMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			propertiesMap.put(key, value);
			System.out.println(key + " => " + value);
		}
		return propertiesMap;
	}

	public static void setMapToProperty(Map Map) throws FileNotFoundException, IOException {
		OutputStream output = JSCHutil.getOutputStream();
		Properties properties = JSCHutil.getProperties();
		Map<String, String> propertiesMap = (Map<String, String>) Map;
		try {
			for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
				properties.setProperty(entry.getKey(), entry.getValue());
			}
			properties.store(output, null);
		} catch (IOException io) {
			jschLogger.JSCHlogger.info("$$$exception occured in copying map to properties " + io);
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean isExist(String folderName, Map propertiesMap) {
		return propertiesMap.containsKey(folderName);
	}

}
