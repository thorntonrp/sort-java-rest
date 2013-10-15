/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.media.image.repository;

import java.io.File;

import static org.lds.stack.utils.FileUtils.file;

/**
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
class LocalFileRepository {

	private final File baseDir;
	private final File cacheDir;

	LocalFileRepository(File baseDir) {
		this.baseDir = baseDir;
		this.cacheDir = file(baseDir, ".cache");
	}

	File getFile(String fileName) {
		return file(baseDir, fileName);
	}

	File getCacheFile(String fileName) {
		return file(cacheDir, fileName);
	}
}
