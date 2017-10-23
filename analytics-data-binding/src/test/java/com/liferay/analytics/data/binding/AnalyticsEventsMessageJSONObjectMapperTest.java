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

package com.liferay.analytics.data.binding;

import com.liferay.analytics.data.binding.internal.AnalyticsEventsMessageJSONObjectMapper;
import com.liferay.analytics.java.client.AnalyticsEventsMessage;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author Marcellus Tavares
 */
public class AnalyticsEventsMessageJSONObjectMapperTest {

	@Test
	public void testJSONDeserialization() throws Exception {
		String jsonString = read("analytics_events_message.json");

		AnalyticsEventsMessage analyticsEventsMessage = _jsonObjectMapper.map(
			jsonString);

		Assert.assertEquals(
			"AnalyticsKey", analyticsEventsMessage.getAnalyticsKey());
		Assert.assertEquals(0, analyticsEventsMessage.getAnonymousUserId());
		Assert.assertEquals(
			"ApplicationId", analyticsEventsMessage.getApplicationId());
		Assert.assertEquals("Channel", analyticsEventsMessage.getChannel());
		Assert.assertEquals("ClientIP", analyticsEventsMessage.getClientIP());

		AnalyticsEventsMessage.Context context =
			analyticsEventsMessage.getContext();

		Assert.assertEquals(0, context.getInstanceId());
		Assert.assertEquals("LanguageId", context.getLanguageId());
		Assert.assertEquals("Url", context.getURL());
		Assert.assertEquals(0, context.getUserId());

		List<AnalyticsEventsMessage.Event> events =
			analyticsEventsMessage.getEvents();

		Assert.assertEquals(events.toString(), 1, events.size());

		AnalyticsEventsMessage.Event event = events.get(0);

		Assert.assertEquals("AdditionalInfo", event.getAdditionalInfo());
		Assert.assertEquals("View", event.getEvent());

		Map<String, String> properties = event.getProperties();

		Assert.assertEquals("v1", properties.get("k1"));
		Assert.assertEquals("v2", properties.get("k2"));

		List<AnalyticsEventsMessage.Referrer> referrers = event.getReferrers();

		Assert.assertEquals(referrers.toString(), 1, referrers.size());

		AnalyticsEventsMessage.Referrer referrer = referrers.get(0);

		Assert.assertEquals(
			Arrays.asList("1", "2", "3"), referrer.getReferrerEntityIds());
		Assert.assertEquals("EntityType", referrer.getReferrerEntityType());

		Calendar calendar = new GregorianCalendar();

		calendar.set(2018, 10, 20, 12, 30, 15);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));

		Assert.assertEquals(calendar.getTime(), event.getTimestamp());

		Assert.assertEquals(
			"MessageFormat", analyticsEventsMessage.getMessageFormat());
		Assert.assertEquals("1.0", analyticsEventsMessage.getProtocolVersion());
		Assert.assertEquals("UserAgent", analyticsEventsMessage.getUserAgent());
	}

	@Test
	public void testJSONSerialization() throws Exception {
		AnalyticsEventsMessage.Builder messageBuilder =
			AnalyticsEventsMessage.builder();

		messageBuilder.analyticsKey("AnalyticsKey");
		messageBuilder.anonymousUserId(0);
		messageBuilder.applicationId("ApplicationId");
		messageBuilder.channel("Channel");
		messageBuilder.clientIP("ClientIP");

		AnalyticsEventsMessage.Context.Builder contextBuilder =
			AnalyticsEventsMessage.Context.builder();

		contextBuilder.instanceId(0);
		contextBuilder.languageId("LanguageId");
		contextBuilder.url("Url");
		contextBuilder.userId(0);

		messageBuilder.context(contextBuilder.build());

		AnalyticsEventsMessage.Event.Builder eventBuilder =
			AnalyticsEventsMessage.Event.builder();

		eventBuilder.additionalInfo("AdditionalInfo");
		eventBuilder.event("View");

		eventBuilder.property("k1", "v1");
		eventBuilder.property("k2", "v2");

		AnalyticsEventsMessage.Referrer.Builder referrerBuilder =
			AnalyticsEventsMessage.Referrer.builder();

		referrerBuilder.referrerEntityIds(Arrays.asList("1", "2", "3"));
		referrerBuilder.referrerEntityType("EntityType");

		eventBuilder.referrer(referrerBuilder.build());

		Calendar calendar = new GregorianCalendar();

		calendar.set(2018, 10, 20, 12, 30, 15);
		calendar.set(Calendar.MILLISECOND, 0);

		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));

		eventBuilder.timestamp(calendar.getTime());

		messageBuilder.event(eventBuilder.build());

		messageBuilder.messageFormat("MessageFormat");
		messageBuilder.protocolVersion("1.0");
		messageBuilder.userAgent("UserAgent");

		String expectedJSONString = read("analytics_events_message.json");

		String actualJSONString = _jsonObjectMapper.map(messageBuilder.build());

		JSONAssert.assertEquals(expectedJSONString, actualJSONString, false);
	}

	protected String read(String fileName) throws Exception {
		Class<?> clazz = getClass();

		URL url = clazz.getResource(fileName);

		byte[] bytes = Files.readAllBytes(Paths.get(url.toURI()));

		return new String(bytes);
	}

	private final JSONObjectMapper<AnalyticsEventsMessage> _jsonObjectMapper =
		new AnalyticsEventsMessageJSONObjectMapper();

}