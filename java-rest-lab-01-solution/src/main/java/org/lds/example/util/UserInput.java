/*
 * Copyright (C) 2013 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.util;

import java.util.List;
import java.util.Map;

import org.lds.stack.maven.input.InputProperty;
import org.lds.stack.maven.input.InputRequest;
import org.lds.stack.utils.CollectionUtils;

/**
 * Utility class for requesting user input.
 *
 * @author Robert Thornton <thorntonrp@ldschurch.org>
 */
public class UserInput {

	public static UserInput withTitle(String title) {
		return new UserInput().title(title);
	}

	public static UserInput withPrompt(String prompt) {
		return new UserInput().title(prompt);
	}

	private final Map<String, InputProperty> properties = CollectionUtils.newMap();
	private String title;
	private String prompt;
	private InputProperty current;
	private boolean enableSystemDefaults;
	private boolean updateSystemProperties;

	public UserInput title(String title) {
		this.title = title;
		this.current = null;
		return this;
	}

	public UserInput applySystemProperties(boolean enableSystemDefaults) {
		this.enableSystemDefaults = enableSystemDefaults;
		this.current = null;
		return this;
	}

	public UserInput updateSystemProperties(boolean updateSystemProperties) {
		this.updateSystemProperties = updateSystemProperties;
		this.current = null;
		return this;
	}

	public UserInput prompt(String prompt) {
		this.prompt = prompt;
		current = null;
		return this;
	}

	public UserInput property(String name) {
		current = new InputProperty().name(name);
		properties.put(name, current);
		return this;
	}

	public UserInput label(String label) {
		checkProperty();
		current.displayName(label);
		return this;
	}

	public UserInput value(String value) {
		checkProperty();
		current.value(value);
		return this;
	}

	public UserInput type(InputProperty.Type type) {
		checkProperty();
		current.type(type);
		return this;
	}

	public UserInput readOnly(boolean readOnly) {
		checkProperty();
		current.readOnly(readOnly);
		return this;
	}

	public UserInput secure(boolean secure) {
		checkProperty();
		current.secure(secure);
		return this;
	}

	public UserInput requestInput() {
		List<InputProperty> inputProperties = CollectionUtils.newList(properties.values());
		if (enableSystemDefaults) {
			for (InputProperty inputProperty : inputProperties) {
				if (inputProperty.getValue() == null) {
					inputProperty.setValue(System.getProperty(inputProperty.getName()));
				}
			}
		}
		InputRequest.requestInput(title, prompt, inputProperties);
		if (updateSystemProperties) {
			for (InputProperty inputProperty : inputProperties) {
				System.setProperty(inputProperty.getName(), inputProperty.getValue());
			}
		}
		return this;
	}

	//-- Private Implementation ----------------------------------------------//

	private void checkProperty() {
		if (current == null) {
			throw new IllegalStateException("No property context. The method " +
					"UserInput.property(String) must be called before " +
					"setting property attributes.");
		}
	}
}
