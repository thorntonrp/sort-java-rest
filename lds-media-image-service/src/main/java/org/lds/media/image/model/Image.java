/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.media.image.model;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
@XmlRootElement
public class Image extends AbstractImageModel {

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "content-type")
	private String contentType;

	Image() {}

	public Image(String id, String name, URL url, String contentType) {
		super(id, name, url);
		this.contentType = contentType;
	}

	public URL getUrl(ImageType imageType) throws MalformedURLException {
		if (imageType == null) {
			return getUrl();
		} else {
			return new URL(getUrl().toString().replace("-thumbnail.", "-" + imageType.name().toLowerCase() + "."));
		}
	}

	public String getContentType() {
		return contentType;
	}
}
