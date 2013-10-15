/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.media.image.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lds.media.image.model.Image;
import org.lds.media.image.model.ImageCollection;
import org.lds.media.image.model.ImageType;
import org.lds.stack.xml.XPathTemplateBuilder;
import org.lds.stack.xml.XmlDataAccessException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static java.util.Collections.emptyMap;
import static org.lds.stack.logging.LogUtils.*;
import static org.lds.stack.utils.CollectionUtils.*;
import static org.lds.stack.utils.FileUtils.*;
import static org.lds.stack.utils.IOUtils.*;
import static org.lds.stack.utils.StringUtils.*;

/**
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
public class ImageRepository {

	private static final Logger LOG = getLogger();

	private final String baseUrl = "http://www.lds.org/media-library/images/";

	private final Map<String, ImageCollection> cachedCollections = newLinkedMap();
	private final Map<String, Map<String, Image>> cachedImages = newMap();

	private final File baseDirectory;
	private final LocalFileRepository repository;

	private boolean offline;

	private boolean initialized;

	//-- Initialization ------------------------------------------------------//
	public ImageRepository() {
		String userHome = System.getProperty("user.home");
		baseDirectory = file(userHome, "Pictures", "LDS Image Gallery");
		repository = new LocalFileRepository(baseDirectory);
	}

	//-- Public API ----------------------------------------------------------//
	public String getId() {
		return "lds-media-library-image-gallery";
	}

	public String getName() {
		return "LDS Media Library Image Gallery";
	}

	public boolean isOffline() {
		return offline;
	}

	public void setOffline(boolean offline) {
		this.offline = offline;
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}

	public List<ImageCollection> getImageCollections() throws IOException {
		return newList(getCachedCollections().values());
	}

	public List<Image> getImagesByCollection(String collectionId) throws IOException {
		Map<String, Image> results = cachedImages.get(collectionId);
		if (results == null) {
			loadCollectionImages(collectionId);
			results = cachedImages.get(collectionId);
			if (results == null) {
				return newList();
			} else {
				return newList(cachedImages.get(collectionId).values());
			}
		}
		return newList(results.values());
	}

	public Image getImage(String collectionId, String imageId) {
		Map<String, Image> collection = cachedImages.get(collectionId);
		if (collection == null) {
			throw new IllegalArgumentException("No such image collection with id: " + collectionId);
		}
		return collection.get(imageId);
	}

	public void downloadImage(String collectionId, String imageId, ImageType imageType) throws IOException {
		Image image = getImage(collectionId, imageId);
		if (image == null) {
			throw new IllegalArgumentException(MessageFormat.format(
					"No such image with collectionId \"{0}\" and imageId \"{1}\"",
					collectionId, imageId));
		} else if (imageType == null) {
			throw new IllegalArgumentException("ImageType is required");
		}
		File localFile = repository.getFile(path(imageType.name().toLowerCase(), collectionId, image.getName()));
		URL url = image.getUrl(imageType);
		if (!localFile.exists()) {
			if (offline) {
				throw new IOException("Repository is offline");
			} else {
				localFile.getParentFile().mkdirs();
				try (FileOutputStream out = new FileOutputStream(localFile);
						InputStream in = url.openStream();) {
					info(LOG, "Downloading {0} ...", url);
					copy(in, out);
				} catch (IOException ex) {
					localFile.delete();
					throw ex;
				}
			}
		} else {
			info(LOG, "File already exists: {0}", localFile);
		}
	}

	public ImageCollection getImageCollection(String collectionId) throws IOException {
		return getCachedCollections().get(collectionId);
	}

	//-- Private Implementation ----------------------------------------------//
	private void initializeCollections() throws IOException {
		String source = readGallerySource();
		List<Node> elements = extractImageCollectionLinks(source);
		for (int i = 0; i < elements.size(); i++) {
			Element element = (Element) elements.get(i);
			String url = substringBefore(element.getAttribute("href"), "?");
			String id = substringAfter(url, baseUrl);
			String name = element.getTextContent();
			createCollection(id, name, new URL(url));
		}
	}

	private String readGallerySource() throws IOException {
		return readHtmlSource(new URL(baseUrl), "index");
	}

	public String readHtmlSource(URL url, String localName) throws IOException {
		File cacheFile = repository.getCacheFile(localName + ".html");
		String source;
		if (!cacheFile.exists()) {
			if (offline) {
				throw new IOException("Repository is offline");
			} else {
				cacheFile.getParentFile().mkdirs();
				source = cleanHtml(read(url, UTF_8));
				write(cacheFile, UTF_8, source);
			}
		} else {
			source = read(cacheFile, UTF_8);
		}
		return source;
	}

	private List<Node> extractImageCollectionLinks(String source) throws XmlDataAccessException {
		String xpath = "//a[starts-with(@href, '" + baseUrl + "')]";
		List<Node> results = new XPathTemplateBuilder()
				.document(new StringReader(source))
				.create()
				.queryForList(xpath, Node.class);
		return results;
	}

	private void createCollection(String id, String name, URL url) {
		ImageCollection collection = cachedCollections.get(id);
		if (collection == null || (isBlank(collection.getName()) && isNotBlank(name))) {
			collection = new ImageCollection(id, name, url);
			cachedCollections.put(collection.getId(), collection);
		}
	}

	private void loadCollectionImages(String collectionId) throws IOException {
		String source = readCollectionSource(collectionId);
		Pattern searchPattern = Pattern.compile("(?s)largeImageMap\\['([^']*)']\\s*=\\s*new\\s+ImageData\\('([^']*)");
		Matcher matcher = searchPattern.matcher(source);
		Map<String, Image> images = newMap();
		while (matcher.find()) {
			String id = matcher.group(1);
			URL url = new URL(matcher.group(2).replace("-gallery.", "-thumbnail."));
			String name = extractFileName(url);
			String ext = extractFileExtension(name);
			String contentType = getContentTypeByFileExtension(ext);
			Image image = new Image(id, name, url, contentType);
			images.put(image.getId(), image);
		}
		if (images.isEmpty()) {
			images = emptyMap();
		}
		cachedImages.put(collectionId, images);
	}

	private String readCollectionSource(String collectionId) throws IOException {
		ImageCollection collection = cachedCollections.get(collectionId);
		if (collection == null) {
			throw new IllegalArgumentException("No such collection having ID: " + collectionId);
		}
		return readHtmlSource(collection.getUrl(), collectionId);
	}

	private static String extractFileName(URL url) {
		String temp = url.getPath();
		int index = temp.lastIndexOf('/');
		if (index >= 0) {
			temp = temp.substring(index + 1).replace("-thumbnail.", ".");
		}
		return temp;
	}

	private static String extractFileExtension(String fileName) {
		String temp = fileName;
		int index = temp.lastIndexOf('.');
		if (index > 0 && index < temp.length()) {
			return temp.substring(index + 1);
		}
		return null;
	}

	private static String getContentTypeByFileExtension(String fileExtension) {
		if (fileExtension == null) {
			return "application/octet-stream";
		}
		return "image/" + fileExtension;
	}

	private static String cleanHtml(String source) {
		String result = source;
		result = result.replaceAll("(<(br|meta|link|img|input)\\b[^>]*?)/?>", "$1/>");
		result = result.replaceAll("<option\\b[^>]*\\bselected\\b(?!>=\"selected\")", "$0=\"selected\"");
		result = result.replaceAll("<script\\b.*?>", "$0<![CDATA[");
		result = result.replaceAll("</script>", "]]>$0");
		return result;
	}

	private Map<String, ImageCollection> getCachedCollections() throws IOException {
		if (!initialized) {
			initializeCollections();
			initialized = true;
		}
		return cachedCollections;
	}
}
