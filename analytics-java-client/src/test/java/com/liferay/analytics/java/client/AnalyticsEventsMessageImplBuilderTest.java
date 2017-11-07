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

package com.liferay.analytics.java.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jeyvison Nascimento
 * @author Marcellus Tavares
 */
public class AnalyticsEventsMessageImplBuilderTest {

	@Test
	public void testCreateEvent() {
		String expectedApplicationId = randomString();
		String expectedEventId = randomString();

		Map<String, String> expectedProperties = new HashMap<>();

		expectedProperties.put(randomString(), randomString());
		expectedProperties.put(randomString(), randomString());
		expectedProperties.put(randomString(), randomString());

		AnalyticsEventsMessageImpl.Event actualEvent = createEvent(
			expectedApplicationId, expectedEventId, expectedProperties);

		assertEvent(
			expectedApplicationId, expectedEventId, expectedProperties,
			actualEvent);
	}

	@Test
	public void testCreateMessage() {

		// Context

		Map<String, String> expectedContext = new HashMap<>();

		expectedContext.put(randomString(), randomString());

		// Events

		List<AnalyticsEventsMessageImpl.Event> expectedEvents =
			new ArrayList<>();

		String expectedApplicationId = randomString();
		String expectedEventId = randomString();

		Map<String, String> expectedProperties = new HashMap<>();

		expectedProperties.put(randomString(), randomString());

		expectedEvents.add(
			createEvent(
				expectedApplicationId, expectedEventId, expectedProperties));

		// Message

		String expectedAnalyticsKey = randomString();
		String expectedUserId = randomString();
		String expectedProtocolVersion = randomString();

		AnalyticsEventsMessageImpl actualAnalyticsEventsMessage =
			createAnalyticsEventsMessage(
				expectedAnalyticsKey, expectedUserId, expectedContext,
				expectedEvents, expectedProtocolVersion);

		Assert.assertEquals(
			expectedAnalyticsKey,
			actualAnalyticsEventsMessage.getAnalyticsKey());
		Assert.assertEquals(
			expectedUserId, actualAnalyticsEventsMessage.getUserId());
		Assert.assertEquals(
			expectedContext, actualAnalyticsEventsMessage.getContext());

		List<AnalyticsEventsMessageImpl.Event> actualEvents =
			actualAnalyticsEventsMessage.getEvents();

		Assert.assertEquals(
			expectedEvents.toString(), expectedEvents.size(),
			actualEvents.size());

		int i = 0;

		for (AnalyticsEventsMessageImpl.Event expectedEvent : expectedEvents) {
			assertEvent(
				expectedEvent.getApplicationId(), expectedEvent.getEventId(),
				expectedEvent.getProperties(), actualEvents.get(i++));
		}

		Assert.assertEquals(
			expectedProtocolVersion,
			actualAnalyticsEventsMessage.getProtocolVersion());
	}

	@Test(expected = IllegalStateException.class)
	public void testCreateMessageWithoutEvent() {
		createAnalyticsEventsMessage(
			randomString(), randomString(), new HashMap<>(), new ArrayList<>(),
			randomString());
	}

	protected void assertEvent(
		String expectedApplicationId, String expectedEventId,
		Map<String, String> expectedProperties,
		AnalyticsEventsMessageImpl.Event actualEvent) {

		Assert.assertEquals(
			expectedApplicationId, actualEvent.getApplicationId());
		Assert.assertEquals(expectedEventId, actualEvent.getEventId());
		Assert.assertEquals(expectedProperties, actualEvent.getProperties());
	}

	protected AnalyticsEventsMessageImpl createAnalyticsEventsMessage(
		String analyticsKey, String userId, Map<String, String> context,
		List<AnalyticsEventsMessageImpl.Event> events, String protocolVersion) {

		AnalyticsEventsMessageImpl.Builder messageBuilder =
			AnalyticsEventsMessageImpl.builder(analyticsKey, userId);

		messageBuilder.context(context);

		for (AnalyticsEventsMessageImpl.Event event : events) {
			messageBuilder.event(event);
		}

		messageBuilder.protocolVersion(protocolVersion);

		return messageBuilder.build();
	}

	protected AnalyticsEventsMessageImpl.Event createEvent(
		String applicationId, String eventId, Map<String, String> properties) {

		AnalyticsEventsMessageImpl.Event.Builder eventBuilder =
			AnalyticsEventsMessageImpl.Event.builder(applicationId, eventId);

		for (Map.Entry<String, String> entry : properties.entrySet()) {
			eventBuilder.property(entry.getKey(), entry.getValue());
		}

		return eventBuilder.build();
	}

	protected String randomString() {
		return RandomStringUtils.random(5);
	}

}