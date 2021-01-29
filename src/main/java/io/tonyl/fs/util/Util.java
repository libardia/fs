package io.tonyl.fs.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

public class Util {
	private Util() {
	}

	public static boolean deleteDirectory(File dir) {
		// Get all the contents of the directory
		File[] contents = dir.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDirectory(f);
			}
		}

		// When we finally reach here, everything has either been deleted, or there was
		// nothing to delete in the first place. In any case, this directory itself can
		// be deleted.
		return dir.delete();
	}

	public static boolean createFile(File f) throws IOException {
		f.getParentFile().mkdirs();
		return f.createNewFile();
	}

	public static void logAction(Logger log, HttpServletRequest request) {
		log.info("REQUEST RECIEVED: {} [from {}]", request.getRequestURI(), request.getRemoteAddr());
	}
}
