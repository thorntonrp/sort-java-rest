/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import static org.lds.stack.logging.LogUtils.*;

/**
 * Main application configuration. Enables component scanning and provides
 * additional configurations sub-classes for profiles.
 *
 * @author Robert Thornton <thorntonrp@ldschurch.org>
 */
@Configuration
@ComponentScan(basePackageClasses = Main.class)
public class MainConfiguration {

	/**
	 * A logger named for this class
	 */
	private static final Logger LOG = getLogger();

	static {
		info(LOG, "Obtained logger: {0}", LOG.getName());
		info(LOG, "Main configuration loading...");
	}

	/**
	 * Provides support for property placeholder expressions in the form ${...}.
	 *
	 * @return A bean for configuring support for property placeholders
	 */
	@Bean
	static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		info(LOG, "Enabling support for property placeholders.");
		return new PropertySourcesPlaceholderConfigurer();
	}

	MainConfiguration() {}

	/**
	 * Default profile configuration
	 */
	@Configuration
	@Profile("!local")
	@PropertySource({"META-INF/spring/default.properties"})
	static class DefaultConfiguration extends BaseProfileConfiguration {
		static { info(LOG, "Loading \"default\" profile..."); }
	}

	/**
	 * Local profile configuration. Loads "default" profile properties first,
	 * then "local" profile properties.
	 */
	@Configuration
	@Profile("local")
	@PropertySource({
		"META-INF/spring/default.properties",
		"META-INF/spring/local.properties"})
	static class LocalConfiguration extends BaseProfileConfiguration {
		static { info(LOG, "Loading \"local\" profile..."); }
	}

	/**
	 * Base profile configuration class. Inject the name of the profile and
	 * prints a message to the log upon initialization to identify the active
	 * profile name.
	 */
	static abstract class BaseProfileConfiguration {

		/**
		 * Injects the active "profile.name" property from the loaded property
		 * resources.
		 */
		@Value("${profile.name}")
		private String profileName;

		/**
		 * Prints a message to the log that identifies the active profile name.
		 */
		@PostConstruct
		void init() {
			info(LOG, "The \"{0}\" profile is now active.", profileName);
		}
	}
}
