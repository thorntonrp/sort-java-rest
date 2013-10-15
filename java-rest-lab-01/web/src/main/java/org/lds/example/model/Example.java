/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.model;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.lds.example.service.VerifyExampleNameNotDuplicate;
import org.lds.stack.security.web.validate.jsr303.SafeHtml;

/**
 * A sample Model object that can be copied to create new Model Objects
 */
public class Example implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long id;
    
    @NotBlank(message="{manageExample.namerequired}")
    @Size(min = 1, max = 200, message="{manageExample.sizebetween}")
    @VerifyExampleNameNotDuplicate
    private String name;
    
    @SafeHtml
    @NotBlank(message="{manageExample.datarequired}")
    private String data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
