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
 * @param <T> The subclass type
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
@XmlType(name = "ImageModel")
public abstract class AbstractImageModel<T extends AbstractImageModel<T>> extends XmlModelObject implements Comparable<T> {

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

	@Override
	public int compareTo(T o) {
		return this.getId().compareTo(o.getId());
	}
}
