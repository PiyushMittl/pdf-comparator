package com.ituple.apps.pdf.comparator.main;

import java.io.File;
import java.io.FileWriter;

import com.ituple.apps.pdf.comparator.Constant;
import com.ituple.apps.pdf.comparator.FileDAOImpl;
import com.ituple.apps.pdf.comparator.filecopy.SFTPmain;
import com.ituple.apps.pdf.comparator.utils.ComparionResultDTO;
import com.ituple.apps.pdf.comparator.utils.PdfComparisonUtils;

public class Main {
	public static void main(String[] args) {
		try {
			SFTPmain.downloadBundleSFTP();
		} catch (Exception e1) {
			System.out.println("Exception occured in downloading files: " + e1);
			e1.printStackTrace();
		}

		StringBuilder csv = new StringBuilder();
		StringBuilder errorCsv = new StringBuilder();
		try {
			PdfComparisonUtils.getHashCodeMap(Constant.INCOMING_PDF_BUNDLE);
			csv.append("Original File").append(",").append("Duplicate File").append("\n");
			if (PdfComparisonUtils.DUPLICATE_FILES_LIST.isEmpty()) {
				csv.append("NO MATCHED PDF FOUND!!!");
			} else {
				for (ComparionResultDTO duplicateDTO : PdfComparisonUtils.DUPLICATE_FILES_LIST) {
					StringBuilder builder = new StringBuilder();
					builder.append(duplicateDTO.getUnixPath(duplicateDTO.getInitialFile())).append(",")
					.append(duplicateDTO.getUnixPath(duplicateDTO.getDuplicateFile())).append("\n");
					csv.append(builder);
				}
			}
			File file = new File(Constant.DUPLICATE_REPORT_LOCATION + System.currentTimeMillis() + ".csv");
			FileWriter writer = new FileWriter(file);
			writer.write(csv.toString());
			writer.flush();
			writer.close();
			for (String errorEntry : PdfComparisonUtils.EXCEPTION_FILES_LIST) {
				errorCsv.append(errorEntry).append("\n");
			}
			File errorFile = new File(Constant.ERROR_REPORT_LOCATION + System.currentTimeMillis() + ".csv");
			FileWriter errorWriter = new FileWriter(errorFile);
			errorWriter.write(errorCsv.toString());
			errorWriter.flush();
			errorWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileDAOImpl.closeConnection();
		}
	}
}
