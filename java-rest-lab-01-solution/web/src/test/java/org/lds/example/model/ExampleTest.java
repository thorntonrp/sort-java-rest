/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.model;

import org.lds.stack.test.TestUtils;
import org.testng.annotations.Test;

@Test
public class ExampleTest {

	public void testGettersAndSetters() {
		TestUtils.testProperties(new Example());
	}
}