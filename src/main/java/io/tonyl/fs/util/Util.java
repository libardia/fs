package io.tonyl.fs.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

public class Util {
	private Util() {
	}

	public static boolean deleteDirectory(Path dir) {
		return deleteDirectory(dir.toFile());
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

	public static void cleanEmptyDirs(Path dir) {
		cleanEmptyDirs(dir.toFile());
	}

	public static void cleanEmptyDirs(File dir) {
		// Get all the contents of the directory
		File[] contents = dir.listFiles();

		// If dir is a file, do nothing
		if (contents == null) {
			return;
		}

		// If dir is a directory, delete all empty directories in it
		for (File f : contents) {
			cleanEmptyDirs(f);
		}

		// Finally, consider dir itself.
		if (dir.listFiles().length == 0) {
			dir.delete();
		}
	}

	public static boolean createFile(File f) throws IOException {
		f.getParentFile().mkdirs();
		return f.createNewFile();
	}

	public static void logAction(Logger log, HttpServletRequest request) {
		log.info("REQUEST RECIEVED: {} [from {}]", request.getRequestURI(), request.getRemoteAddr());
	}
}
