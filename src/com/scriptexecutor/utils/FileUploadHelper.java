package com.scriptexecutor.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileUploadHelper {

	public static String uploadFile(String requestURL, String filePath) {
		StringBuffer responseStr = new StringBuffer();

		String charset = "UTF-8";
		try {
			MultipartUtil multipart = new MultipartUtil(requestURL, charset);

			multipart.addFilePart("fileUpload", new File(filePath));

			List<String> response = multipart.finish();

			System.out.println("SERVER REPLIED:" + Arrays.toString(response.toArray()));

			for (String line : response) {
				responseStr.append(line);
				System.out.println(line);
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}

		return responseStr.toString();
	}
}