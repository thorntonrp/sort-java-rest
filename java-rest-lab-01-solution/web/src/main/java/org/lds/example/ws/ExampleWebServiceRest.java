/*
 * Copyright 2012 Intellectual Reserve, Inc. All rights reserved.
 * This notice may not be removed.
 */
package org.lds.example.ws;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.lds.example.model.Example;
import org.lds.example.service.ExampleService;
import org.lds.example.ws.model.ExampleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * An implementation of ExampleWebService using Jax-rs
 */
@Controller
@Scope("singleton")
@Path("/example")
public class ExampleWebServiceRest {

	@Autowired
	private ExampleService exampleService;

	@POST
	@Produces({"application/json", "application/xml"})
	@Consumes({"application/json", "application/xml"})
	public void createExample(ExampleDto exampleDto) {
		try {
			Example example = new Example();
			example.setName(exampleDto.getName());
			example.setData(exampleDto.getData());
			exampleService.createExample(example);
		} catch(ConstraintViolationException e) {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("Validation error(s) occured while saving Example:\n");
			for(ConstraintViolation<?> violation : e.getConstraintViolations()) {
				errorMessage.append("* ").append(violation.getMessage()).append("\n");
			}
			Response response = Response.status(Status.BAD_REQUEST).entity(errorMessage.toString()).build();
			throw new WebApplicationException(response);
		}
	}

	@GET
	@Produces({"application/json", "application/xml"})
	public List<ExampleDto> getExamples() {
		List<Example> exampleEntities = exampleService.getAllExamples();
		List<ExampleDto> exampleDtos = new ArrayList<ExampleDto>();
		for(Example example : exampleEntities) {
			ExampleDto exampleDto = convertExampleToExampleDto(example);
			exampleDtos.add(exampleDto);
		}
		return exampleDtos;
	}

	@GET
	@Path("/{exampleId}")
	@Produces({"application/json", "application/xml"})
	public ExampleDto getExample(@PathParam("exampleId") Long id) {
		Example exampleEntity = exampleService.findExample(id);
		ExampleDto exampleDto = convertExampleToExampleDto(exampleEntity);
		if(exampleDto == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return exampleDto;
	}

	private ExampleDto convertExampleToExampleDto(Example example) {
		if(example == null) {
			return null;
		}
		ExampleDto exampleDto = new ExampleDto();
		exampleDto.setId(example.getId());
		exampleDto.setName(example.getName());
		exampleDto.setData(example.getData());
		return exampleDto;
	}
}
