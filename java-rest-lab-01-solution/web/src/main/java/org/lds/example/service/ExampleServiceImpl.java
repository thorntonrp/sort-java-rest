/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang.Validate;
import org.lds.example.model.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
@Service("exampleService")
public class ExampleServiceImpl implements ExampleService {

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert exampleModelJdbcInsert;
	private Validator validator; 

	ExampleServiceImpl() {
		//In case cglib is being used.
	}

	@Inject
	public ExampleServiceImpl(JdbcTemplate jdbcTemplate, Validator validator) {
		this.jdbcTemplate = jdbcTemplate;
		this.validator = validator;
		this.exampleModelJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("EXAMPLE");
		this.exampleModelJdbcInsert.setOverrideIncludeSynonymsDefault(true);
	}

	/* (non-Javadoc)
	 * @see org.lds.example.service.ExampleService#createExample(org.lds.example.model.Example)
	 */
	@Override
	@Transactional
	public void createExample(Example example) throws ConstraintViolationException {
		Validate.notNull(example, "Example instance must not be null");
		Set<ConstraintViolation<Example>> violations = validator.validate(example);

		if(!violations.isEmpty()) {
			throw new ConstraintViolationException(new LinkedHashSet<ConstraintViolation<?>>(violations));
		}
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("EXAMPLE_NAME", example.getName());
		parameters.put("DATA", example.getData());
		exampleModelJdbcInsert.execute(parameters);
	} 

	/* (non-Javadoc)
	 * @see org.lds.example.service.ExampleService#getAllExamples()
	 */
	@Override
	public List<Example> getAllExamples() {
		return jdbcTemplate.query("select * from EXAMPLE", new ExampleRowMapper());
	}

	/* (non-Javadoc)
	 * @see org.lds.example.service.ExampleService#findExample(java.lang.Long)
	 */
	@Override
	public Example findExample(Long id) {
		List<Example> examples = jdbcTemplate.query("select * from EXAMPLE where ID=?", new ExampleRowMapper(), id);
		if(examples.isEmpty()) {
			return null;
		}
		return examples.get(0);
	}

	public Example findExample(String name) {
		return jdbcTemplate.queryForObject("select * from EXAMPLE where EXAMPLE_NAME=?", new ExampleRowMapper(), name);
	}

	private static class ExampleRowMapper implements RowMapper<Example> {
		@Override
		public Example mapRow(ResultSet rs, int rowNum) throws SQLException {
			Example example = new Example();
			example.setId(rs.getLong("ID"));
			example.setName(rs.getString("EXAMPLE_NAME"));
			example.setData(rs.getString("DATA"));
			return example;
		}
	}

	public static class VerifyExampleNameNotDuplicateValidator implements ConstraintValidator<VerifyExampleNameNotDuplicate, String> {

		@Autowired
		private JdbcTemplate jdbcTemplate;

		@Override
		public void initialize(VerifyExampleNameNotDuplicate constraintAnnotation) {	}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return jdbcTemplate.queryForInt("select count(*) from EXAMPLE where EXAMPLE_NAME=?", value) == 0;
		}
	}
}
