/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.settings;

import org.lds.stack.qa.TestConfig;

/**
 * A container for test environment property injection. {@link TestConfig}
 * will initialize each public, static, non-final field with a matching test
 * environment property. The test environment properties are selected based on
 * the value of the "testEnv" system property.
 * 
 * @see TestConfig
 *
 * @author <a href="http://code.lds.org/maven-sites/stack/">Stack Starter</a>
 */
public final class Constants {

	// The endpoint of the the example REST web service
    public static String restEndpointUrl;

	// The full URL to target application's login page
	public static String loginUrl;

	// A test user to be authenticated
	public static String username;

	// Password to the test user account
	public static String password;

	static {
		// Load test environment properties using the "testEnv" System property.
		new TestConfig().applyConfiguration(Constants.class);
	}

	// Private constructor to discourage instantiation.
	private Constants() {}
}
