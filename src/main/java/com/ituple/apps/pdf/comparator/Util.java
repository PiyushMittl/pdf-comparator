package com.ituple.apps.pdf.comparator;

import java.sql.Connection;
import java.sql.DriverManager;

public class Util {

	public static Connection getConnection() {

		Connection connection = null;
		try {
			Class.forName(Constant.DRIVER_CLASS_NAME);
			connection = DriverManager.getConnection(Constant.DB_URL, Constant.DB_USER, Constant.DB_PASS);
		} catch (Exception e) {
		}
		return connection;
	}

}
