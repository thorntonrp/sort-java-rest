/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.lds.example.model.Example;

/**
 * A Sample Service Interface
 */
public interface ExampleService {

	/**
	 * Persists an instance of {@code Example} to the Database.
	 * @param example
	 * @throws ConstraintViolationException
	 */
	public void createExample(Example example) throws ConstraintViolationException; 

	/**
	 * Returns all {@code Example} entities from the database.
	 * @return
	 */
	public List<Example> getAllExamples();

	/**
	 * Find an {@code Example} given a primary key
	 * @param id
	 * @return an {@code Example} instance or null if none found.
	 */
	public Example findExample(Long id);

	/**
	 * Find an {@code Example} given a name
	 * @param name
	 * @return an {@code Example} instance or null if none found.
	 */
	public Example findExample(String name);
}
