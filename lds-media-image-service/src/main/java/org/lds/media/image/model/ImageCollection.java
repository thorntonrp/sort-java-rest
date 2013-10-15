/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.media.image.model;

import java.net.URL;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import static org.lds.stack.utils.CollectionUtils.newList;

/**
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
@XmlRootElement(name = "image-collection")
public class ImageCollection extends AbstractImageModel {

	private static final long serialVersionUID = 1L;

	@XmlElementRef
	private final List<Image> images = newList();

	ImageCollection() {}

	public ImageCollection(String id, String name, URL url) {
		super(id, name, url);
	}

	public List<Image> getImages() {
		return images;
	}
}
