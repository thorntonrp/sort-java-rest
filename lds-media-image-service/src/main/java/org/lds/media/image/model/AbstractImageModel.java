/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.media.image.model;

import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.lds.stack.xml.XmlModelObject;

/**
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
@XmlType(name = "ImageModel")
public abstract class AbstractImageModel extends XmlModelObject {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String name;

	@XmlAttribute
	private URL url;

	AbstractImageModel() {}

	AbstractImageModel(String id, String name, URL url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public URL getUrl() {
		return url;
	}
}
