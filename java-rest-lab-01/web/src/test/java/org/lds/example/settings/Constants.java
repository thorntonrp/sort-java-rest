/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.settings;

import org.lds.stack.qa.TestConfig;

public final class Constants {

	static {
		// Load test environment properties using the "testEnv" System property.
		new TestConfig().applyConfiguration(Constants.class);
	}

	public static String dsUrl;
	public static String dsUser;
	public static String dsPassword;

	private Constants() {}
}
