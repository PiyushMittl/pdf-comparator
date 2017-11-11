package com.ituple.apps.pdf.comparator.utils;

public class ComparionResultDTO {

	private String initialFile;
	private String duplicateFile;
	private String duplicateFileSize;
	private String initialFileSize;

	public String getInitialFile() {
		return initialFile;
	}

	public void setInitialFile(String initialFile) {
		this.initialFile = initialFile;
	}

	public String getDuplicateFile() {
		return duplicateFile;
	}

	public void setDuplicateFile(String duplicateFile) {
		this.duplicateFile = duplicateFile;
	}

	public String getDuplicateFileSize() {
		return duplicateFileSize;
	}

	public void setDuplicateFileSize(String duplicateFileSize) {
		this.duplicateFileSize = duplicateFileSize;
	}

	public String getInitialFileSize() {
		return initialFileSize;
	}

	public void setInitialFileSize(String initialFileSize) {
		this.initialFileSize = initialFileSize;
	}

	public String getUnixPath(String windowsPath) {
		return windowsPath
				.replace("\\\\gfrgnas1\\medicalrecordsreview\\TVMFlintScrub\\dump", "/InboundToFilebound/Records")
				.replace("\\", "/");
	}
}
