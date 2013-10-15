/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.media.image.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 *
 * @author Robert Thornton <robert.p.thornton@gmail.com>
 */
public class TestImageCollection {

	@Test
	public void test() throws MalformedURLException {
		ImageCollection imageCollection = new ImageCollection("foo", "foo.jpg", new URL("http://foo.com/foo-thumbnail.jpg"));
		assertEquals(imageCollection.toString(), "<image-collection id=\"foo\" name=\"foo.jpg\" url=\"http://foo.com/foo-thumbnail.jpg\"/>");
	}
}
