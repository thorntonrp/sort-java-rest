/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.RandomStringUtils;
import org.lds.example.model.Example;
import org.lds.example.service.ExampleService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath*:META-INF/spring/*Context.xml","classpath*:META-INF/spring/*Context-test.xml"})
public class ExampleIT extends AbstractTransactionalTestNGSpringContextTests {

	@Inject
	private ExampleService exampleService;

	@Test(groups="smoke")
	public void testManageExample() {
		Example example = new Example();
		String name = RandomStringUtils.randomAlphanumeric(10);
		example.setName(name);
		example.setData("SomeTestData");
		exampleService.createExample(example);
		Assert.assertNotNull(exampleService.findExample(name));
		try {
			exampleService.createExample(example);
		} catch(ConstraintViolationException e) {
			//Success
		}
	}
}
