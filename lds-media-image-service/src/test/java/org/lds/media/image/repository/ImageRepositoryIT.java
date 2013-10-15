/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.media.image.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.lds.media.image.model.Image;
import org.lds.media.image.model.ImageCollection;
import org.lds.media.image.model.ImageType;
import org.testng.annotations.Test;

import static org.lds.stack.utils.FileUtils.file;
import static org.testng.Assert.*;
import static org.testng.FileAssert.*;

/**
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
public class ImageRepositoryIT {

	@Test
	public void test() throws IOException {
		File repoDir = file(System.getProperty("user.home"), "Pictures", "LDS Image Gallery");

		File galleryFile        = file(repoDir, ".cache",    "index.html");
		File collectionFile     = file(repoDir, ".cache",    "animals.html");

		File imageGalleryFile   = file(repoDir, "gallery",   "animals", "bison-762423.jpg");
		File imageMobileFile    = file(repoDir, "mobile",    "animals", "bison-762423.jpg");
		File imagePrintFile     = file(repoDir, "print",     "animals", "bison-762423.jpg");
		File imageThumbnailFile = file(repoDir, "thumbnail", "animals", "bison-762423.jpg");
		File imageWallpaperFile = file(repoDir, "wallpaper", "animals", "bison-762423.jpg");

		assertTrue(!galleryFile.exists() || galleryFile.delete(), "Failed to delete " + galleryFile);
		assertTrue(!collectionFile.exists() || collectionFile.delete(), "Failed to delete " + collectionFile);
		assertTrue(!imageGalleryFile.exists() || imageGalleryFile.delete(), "Failed to delete " + imageGalleryFile);
		assertTrue(!imageMobileFile.exists() || imageMobileFile.delete(), "Failed to delete " + imageMobileFile);
		assertTrue(!imagePrintFile.exists() || imagePrintFile.delete(), "Failed to delete " + imagePrintFile);
		assertTrue(!imageThumbnailFile.exists() || imageThumbnailFile.delete(), "Failed to delete " + imageThumbnailFile);
		assertTrue(!imageWallpaperFile.exists() || imageWallpaperFile.delete(), "Failed to delete " + imageWallpaperFile);

		ImageRepository repository = new ImageRepository();
		List<ImageCollection> collections = repository.getImageCollections();
		for (int i = 0; i < collections.size(); i++) {
			ImageCollection collection = collections.get(i);
			System.out.printf("[%1$3d] %2$-40s:\t%3$s%n", i++, collection.getName(), collection.getUrl());
		}
		assertFile(galleryFile);

		List<Image> images = repository.getImagesByCollection("animals");
		for (int i = 0; i < images.size(); i++) {
			Image image = images.get(i);
			System.out.printf("[%1$2d] %2$-40s:\t%3$s%n", i, image.getName(), image.getUrl());
		}
		assertFile(collectionFile);

		repository.downloadImage("animals", "bison-762423", ImageType.GALLERY);
		assertFile(imageGalleryFile);
		repository.downloadImage("animals", "bison-762423", ImageType.MOBILE);
		assertFile(imageMobileFile);
		repository.downloadImage("animals", "bison-762423", ImageType.PRINT);
		assertFile(imagePrintFile);
		repository.downloadImage("animals", "bison-762423", ImageType.THUMBNAIL);
		assertFile(imageThumbnailFile);
		repository.downloadImage("animals", "bison-762423", ImageType.WALLPAPER);
		assertFile(imageWallpaperFile);
	}
}
