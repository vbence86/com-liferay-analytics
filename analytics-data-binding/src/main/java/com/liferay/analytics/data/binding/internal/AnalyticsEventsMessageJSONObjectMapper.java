/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.analytics.data.binding.internal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.liferay.analytics.data.binding.JSONObjectMapper;
import com.liferay.analytics.java.client.AnalyticsEventsMessageImpl;

import java.io.IOException;

import java.text.SimpleDateFormat;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = {
		"model=com.liferay.analytics.java.client.AnalyticsEventsMessage"
	},
	service = JSONObjectMapper.class
)
public class AnalyticsEventsMessageJSONObjectMapper
	implements JSONObjectMapper<AnalyticsEventsMessageImpl> {

	@Override
	public String map(AnalyticsEventsMessageImpl analyticsEventsMessage)
		throws IOException {

		return _objectMapper.writeValueAsString(analyticsEventsMessage);
	}

	@Override
	public AnalyticsEventsMessageImpl map(String jsonString)
		throws IOException {

		return _objectMapper.readValue(
			jsonString, AnalyticsEventsMessageImpl.class);
	}

	private final ObjectMapper _objectMapper = new ObjectMapper();

	{

		_objectMapper.configure(
			DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		_objectMapper.configure(
			SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		_objectMapper.setDateFormat(
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}

}