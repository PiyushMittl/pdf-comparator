package com.ituple.apps.pdf.comparator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ituple.apps.pdf.comparator.Constant;
import com.ituple.apps.pdf.comparator.FileDAOImpl;
import com.ituple.apps.pdf.comparator.FileDTO;

public class PdfComparisonUtils {
	public static Logger _LOGGER = Logger.getLogger("com.ituple.apps.pdf.comparator.main.PdfComparisonUtils");
	public static Map<String, String> REPO_FILES_MAP = new HashMap<String, String>();
	public static List<ComparionResultDTO> DUPLICATE_FILES_LIST = new ArrayList<ComparionResultDTO>();
	public static List<String> EXCEPTION_FILES_LIST = new ArrayList<String>();
	public static void getHashCodeMap(String rootLocation) throws Exception {
		File directoryFiles = new File(rootLocation);
		File[] files = directoryFiles.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				String fileLocation = file.getCanonicalPath();
				getHashCodeMap(fileLocation);
			} else {
				try {
					String hashCode = getMD5Hash(file.getCanonicalPath());

					FileDTO fileDtoInsert = new FileDTO();
					fileDtoInsert.setHashCode(hashCode);
					fileDtoInsert.setFileName(file.getCanonicalPath().toString().substring(
							file.getCanonicalPath().toString().lastIndexOf('\\') + 1,
							file.getCanonicalPath().toString().lastIndexOf('.')));
					fileDtoInsert.setFilePath(file.getCanonicalPath());

					Object object = FileDAOImpl.search(fileDtoInsert);

					if (object instanceof FileDTO) {
						FileDTO fileDTO = (FileDTO) object;
						ComparionResultDTO dto = new ComparionResultDTO();
						dto.setDuplicateFile(file.getCanonicalPath());
						dto.setInitialFile(fileDTO.getFilePath());
						DUPLICATE_FILES_LIST.add(dto);
						_LOGGER.info("duplicate file:------------> " + file.getCanonicalPath());
					} else if (!Constant.CHECK_ONLY_DB.equals("true")) {
						REPO_FILES_MAP.put(hashCode, file.getCanonicalPath());
						FileDAOImpl.insert(fileDtoInsert);
						_LOGGER.info("##################### insert call");
					}
				} catch (Exception e) {
					EXCEPTION_FILES_LIST.add(file.getCanonicalPath());
				}
			}
		}

	}

	public static String getMD5Hash(String fileLoc) throws Exception {

		StringBuffer hexString = new StringBuffer();
		MessageDigest md;
		FileInputStream fis;

		md = MessageDigest.getInstance("MD5");
		fis = new FileInputStream(fileLoc);
		byte[] dataBytes = new byte[1024];

		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		}
		byte[] mdbytes = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		// convert the byte to hex format method 2
		for (int i = 0; i < mdbytes.length; i++) {
			String hex = Integer.toHexString(0xff & mdbytes[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}

		FileDTO fileDto = new FileDTO();
		fileDto.setFileName(fileLoc.substring(fileLoc.lastIndexOf('\\') + 1, fileLoc.lastIndexOf('.')));
		fileDto.setFilePath(fileLoc);
		fileDto.setHashCode(hexString.toString());
		_LOGGER.info("File processed: " + REPO_FILES_MAP.size() + "   Digest(in hex format):: " + hexString.toString());
		fis.close();
		return hexString.toString();
	}

}
