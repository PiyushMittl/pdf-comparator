package com.ituple.apps.pdf.comparator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ituple.apps.pdf.comparator.logging.ComparatorLogger;

public class FileDAOImpl {
	static Connection conn = Util.getConnection();
	public static void main(String args[]) {
		// createTable();
		// insert(new FileDTO());
		// select();
		// update();
		// delete();
		// search(new FileDTO());
	}

	public static Object search(FileDTO fileDto) {
		Statement stmt = null;
		FileDTO fileDTO = new FileDTO();
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String sql = "SELECT FILENAME,FILELOCATION,HASHCODE from FILE_INFO where HASHCODE='" + fileDto.getHashCode()
					+ "';";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				fileDTO.setFileName(rs.getString("FILENAME"));
				fileDTO.setFilePath(rs.getString("FILELOCATION"));
				fileDTO.setHashCode(rs.getString("HASHCODE"));
			} else {
				return "RECORD_NOT_FOUND";
			}
		} catch (Exception e) {
			rollBack();
			ComparatorLogger.comparatorLogger.info(e.getClass().getName() + ": " + e.getMessage());
		}
		return fileDTO;
	}

	public static void deleteOperation(FileDTO fileDto) {
		Statement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String sql = "DELETE from FILE_INFO where HASHCODE='" + fileDto.getHashCode() + "';";
			ResultSet rs = stmt.executeQuery(sql);
			conn.commit();
			stmt.executeQuery(sql);
		} catch (Exception e) {
			rollBack();
			ComparatorLogger.comparatorLogger.info(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static void updateOperation(FileDTO fileDto) {

		Statement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String sql = "UPDATE from FILE_INFO where HASHCODE='" + fileDto.getHashCode() + "';";
			stmt.executeUpdate(sql);
			stmt.executeQuery(sql);
			stmt.close();
			conn.commit();
		} catch (Exception e) {
			rollBack();
			ComparatorLogger.comparatorLogger.info(e.getClass().getName() + ": " + e.getMessage());
		}
		ComparatorLogger.comparatorLogger.info("Operation done successfully");
	}

	public static void select() {
		Statement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM FILE_INFO;");
			while (rs.next()) {
				String fileName = rs.getString("FILENAME");
				String fileLocation = rs.getString("FILELOCATION");
				String hashCode = rs.getString("HASHCODE");

				ComparatorLogger.comparatorLogger.info("FILENAME = " + fileName);
				ComparatorLogger.comparatorLogger.info("FILELOCATION = " + fileLocation);
				ComparatorLogger.comparatorLogger.info("HASHCODE = " + hashCode);
			}
			rs.close();
			stmt.close();
			conn.commit();
		} catch (Exception e) {
			rollBack();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		ComparatorLogger.comparatorLogger.info("Operation done successfully");
	}

	public static void createTable() {
		Statement stmt = null;
		try {
			ComparatorLogger.comparatorLogger.info("Opened database successfully");
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String sql = "CREATE TABLE FILE_INFO " + "(HASHCODE TEXT PRIMARY KEY     NOT NULL,"
					+ " FILENAME           TEXT    NOT NULL, " + " FILELOCATION            TEXT     NOT NULL) ";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.commit();
		} catch (Exception e) {
			rollBack();
			ComparatorLogger.comparatorLogger.info(e.getClass().getName() + ": " + e.getMessage());
		}
		ComparatorLogger.comparatorLogger.info("Table created successfully");
	}

	public static void insert(FileDTO fileDto) {
		Statement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String sql = "INSERT INTO FILE_INFO (FILELOCATION,FILENAME,HASHCODE) " + "VALUES ('" + fileDto.getFilePath()
					+ "', '" + fileDto.getFileName() + "','" + fileDto.hashCode + "');";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.commit();
		} catch (Exception e) {
			rollBack();
			ComparatorLogger.comparatorLogger.info(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public static void rollBack() {
		try {
			conn.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			conn.close();
		} catch (Exception e) {
			ComparatorLogger.comparatorLogger.info(e.getClass().getName() + ": " + e.getMessage());
		}
	}

}
