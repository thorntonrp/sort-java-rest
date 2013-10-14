/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Robert Thornton <thorntonrp@ldschurch.org>
 */
public class SpringSupport {

	/**
	 * A System property defined by the Spring Framework for supplying the
	 * active configuration profile from which component beans will be loaded
	 * in preference to those in the "default" profile.
	 */
	public static final String PROP_SPRING_PROFILES_ACTIVE = "spring.profiles.active";

	public static ApplicationContext initSpring(Class<?>... configClasses) {
		return new AnnotationConfigApplicationContext(configClasses);
	}

	public static void chooseActiveSpringProfile() {
		// Check if the active Spring profile has already bean set in the
		// system properties
		String profiles = System.getProperty(PROP_SPRING_PROFILES_ACTIVE);
		if (profiles == null) {
			// Prompt the user to supply the active Spring profile and update
			// system properties with the selected profile
			UserInput.withTitle("Input")
					.prompt("Choose the active profile:")
					.updateSystemProperties(true)
					.property(PROP_SPRING_PROFILES_ACTIVE)
					.label("Profile")
					.value("default")
					.requestInput();
		}
	}

	private SpringSupport() {
	}
}
