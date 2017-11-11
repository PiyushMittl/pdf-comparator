package com.ituple.apps.pdf.comparator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constant {
	public static String INCOMING_PDF_BUNDLE;
	public static String DUPLICATE_REPORT_LOCATION;
	public static String ERROR_REPORT_LOCATION;
	public static String JSCH_LOG_LOCATION;
	public static String COMPARATOR_LOG_LOCATION;
	public static String DRIVER_CLASS_NAME;
	public static String DB_URL;
	public static String DB_USER;
	public static String DB_PASS;
	public static String FILENAME = "FILENAME";
	public static String FILELOCATION = "FILELOCATION";
	public static String HASHCODE = "HASHCODE";
	public static String SFTPHOST;
	public static String SFTPPORT;
	public static String SFTPUSER;
	public static String SFTPPASS;
	public static String SFTPWORKINGDIR;
	public static String CHECK_ONLY_DB;
	static {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("C:\\DUMP\\pdfComparator.properties");
			prop.load(input);
			INCOMING_PDF_BUNDLE = prop.getProperty("INCOMING_PDF_BUNDLE").toString().trim();
			DUPLICATE_REPORT_LOCATION = prop.getProperty("DUPLICATE_REPORT_LOCATION").toString().trim();
			ERROR_REPORT_LOCATION = prop.getProperty("ERROR_REPORT_LOCATION").toString().trim();
			JSCH_LOG_LOCATION = prop.getProperty("JSCH_LOG_LOCATION").toString().trim();
			COMPARATOR_LOG_LOCATION = prop.getProperty("COMPARATOR_LOG_LOCATION").toString().trim();
			DRIVER_CLASS_NAME = prop.getProperty("DRIVER_CLASS_NAME").toString().trim();
			DB_URL = prop.getProperty("DB_URL").toString().trim();
			DB_USER = prop.getProperty("DB_USER").toString().trim();
			DB_PASS = prop.getProperty("DB_PASS").toString().trim();
			SFTPHOST = prop.getProperty("SFTPHOST").toString().trim();
			SFTPPORT = prop.getProperty("SFTPPORT").toString().trim();
			SFTPUSER = prop.getProperty("SFTPUSER").toString().trim();
			SFTPPASS = prop.getProperty("SFTPPASS").toString().trim();
			SFTPWORKINGDIR = prop.getProperty("SFTPWORKINGDIR").toString().trim();
			CHECK_ONLY_DB = prop.getProperty("CHECK_ONLY_DB").toString().trim();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
