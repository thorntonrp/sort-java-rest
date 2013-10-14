/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example;

import static org.lds.example.util.SpringSupport.*;

/**
 * Application entry point. Starts up the Spring application context and
 * invokes the main controller.
 *
 * @author Robert Thornton <thorntonrp@ldschurch.org>
 */
public class Main {

	/**
	 * Application entry point. Starts up the Spring application context and
	 * invokes the main controller.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		// (Optional)
		// TODO: Uncomment the following line to prompt the user to choose an
		//       active profile if none has been supplied
		//chooseActiveSpringProfile();

		// Initialize the application context from the configuration class(es)
		initSpring(MainConfiguration.class);
	}

	private Main() {}
}