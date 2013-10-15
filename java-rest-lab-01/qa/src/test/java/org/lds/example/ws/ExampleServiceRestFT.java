/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.ws;

import org.apache.commons.lang.RandomStringUtils;
import org.lds.example.settings.Constants;
import org.lds.example.ws.model.ExampleDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"/META-INF/spring/applicationContext-test.xml"})
public class ExampleServiceRestFT extends AbstractTestNGSpringContextTests  {

	@Test(groups="smoke")
	public void testCreateExample() {
		ExampleDto exampleDto = new ExampleDto();
		String randomName = RandomStringUtils.randomAlphanumeric(10);
		exampleDto.setName(randomName);
		exampleDto.setData(RandomStringUtils.randomAlphanumeric(20));

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		// Post a new entity
		restTemplate.postForLocation(Constants.restEndpointUrl + "/example",
				new HttpEntity<ExampleDto>(exampleDto, headers));

		// Query all entities
		ResponseEntity<?> allExamples = restTemplate.exchange(
				Constants.restEndpointUrl + "/example",
				HttpMethod.GET,
				new HttpEntity<Object>(headers),
				new ExampleDto[0].getClass());
		ExampleDto[] examples = (ExampleDto[]) allExamples.getBody();

		Assert.assertTrue(examples.length >= 1);

		// Find the entity we just added
		ExampleDto myExample = null;
		for (ExampleDto example : examples) {
			if(example.getName().equals(randomName)) {
				myExample = example;
				break;
			}
		}

		// Validate that the entity exists
		Assert.assertNotNull(myExample);
	}

	@Test(dependsOnMethods="testCreateExample")
	public void testGetAllExamples() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		ResponseEntity<?> allExamples = restTemplate.exchange(
				Constants.restEndpointUrl+"/example",
				HttpMethod.GET,
				new HttpEntity<Object>(headers),
				new ExampleDto[0].getClass());
		ExampleDto[] examples = (ExampleDto[]) allExamples.getBody();

		Assert.assertTrue(examples.length >= 1);
	}
}
