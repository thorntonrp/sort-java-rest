/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import static org.lds.stack.logging.LogUtils.*;

/**
 *
 * @author Robert Thornton <thorntonrp@ldschurch.org>
 */
@Controller
@Lazy(false)
public class Application {

	private static final Logger LOG = getLogger();

	@PostConstruct
	public void initialize() {
		info(LOG, "Starting up...");

		// TODO write your application
		info(LOG, "\n--------------------------------------------------------" +
				  "\n HELLO, WORLD!" +
				  "\n--------------------------------------------------------");

		info(LOG, "Shutting down...");
	}
}
