package com.wuzhou.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipUtils {

	public static void main(String[] args) throws Exception {
		extractFolder("j:\\6620fcac-a1e4-41a2-be1c-eaf5f6fc55ab.zip", "j:\\test");
	}

	public static void extractFolder(String zipFile, String unzipPath) throws ZipException, IOException {
		System.out.println(zipFile);
		int BUFFER = 2048;
		File file = new File(zipFile);
		ZipFile zip = new ZipFile(file);
		// String newPath = zipFile.substring(0, zipFile.length() - 4);
		// new File(newPath).mkdir();
		Enumeration zipFileEntries = zip.entries();
		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			System.out.println("==" + currentEntry);
			File destFile = new File(unzipPath, currentEntry);
			// destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}

			if (currentEntry.endsWith(".zip")) {
				extractFolder(destFile.getAbsolutePath(), unzipPath);
			}
			if(currentEntry.endsWith(".gz")) {
				gunzipIt(destFile.getAbsolutePath(), unzipPath);
			}
		}
	}

	/**
	 * 解压gz
	 * un gz
	 * @param gzFilePath
	 * @param newPath
	 */
	private static void gunzipIt(String gzFilePath, String newPath) {
		byte[] buffer = new byte[1024];
		try {
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(gzFilePath));
			FileOutputStream out = new FileOutputStream(new File(newPath+"\\tt.txt"));
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			gzis.close();
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
