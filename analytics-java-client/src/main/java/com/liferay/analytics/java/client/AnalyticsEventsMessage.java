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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eduardo Garcia
 * @see com.liferay.lcs.messaging.AnalyticsEventsMessage
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
	include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "messageFormat",
	use = JsonTypeInfo.Id.NAME, visible = true
)
public class AnalyticsEventsMessage implements Serializable {

	public static AnalyticsEventsMessage.Builder builder() {
		return new AnalyticsEventsMessage.Builder();
	}

	public String getAnalyticsKey() {
		return _analyticsKey;
	}

	public Map<String, String> getContext() {
		return Collections.unmodifiableMap(_context);
	}

	public List<Event> getEvents() {
		return _events;
	}

	public String getProtocolVersion() {
		return _protocolVersion;
	}

	public long getUserId() {
		return _userId;
	}

	public static class Builder {

		public Builder analyticsKey(String analyticsKey) {
			_analyticsEventsMessage._analyticsKey = analyticsKey;

			return this;
		}

		public AnalyticsEventsMessage build() {
			return _analyticsEventsMessage;
		}

		public Builder context(Map<String, String> context) {
			_analyticsEventsMessage._context = context;

			return this;
		}

		public Builder contextProperty(String key, String value) {
			_analyticsEventsMessage._context.put(key, value);

			return this;
		}

		public Builder event(Event event) {
			_analyticsEventsMessage._events.add(event);

			return this;
		}

		public Builder protocolVersion(String protocolVersion) {
			_analyticsEventsMessage._protocolVersion = protocolVersion;

			return this;
		}

		public Builder userId(long userId) {
			_analyticsEventsMessage._userId = userId;

			return this;
		}

		private final AnalyticsEventsMessage _analyticsEventsMessage =
			new AnalyticsEventsMessage();

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Event {

		public static Event.Builder builder() {
			return new Event.Builder();
		}

		public String getApplicationId() {
			return _applicationId;
		}

		public String getEventId() {
			return _eventId;
		}

		public Map<String, String> getProperties() {
			return Collections.unmodifiableMap(_properties);
		}

		public static class Builder {

			public Event.Builder applicationId(String applicationId) {
				_event._applicationId = applicationId;

				return this;
			}

			public AnalyticsEventsMessage.Event build() {
				return _event;
			}

			public Event.Builder eventId(String eventId) {
				_event._eventId = eventId;

				return this;
			}

			public Event.Builder properties(Map<String, String> properties) {
				_event._properties = properties;

				return this;
			}

			public Event.Builder property(String key, String value) {
				_event._properties.put(key, value);

				return this;
			}

			private final AnalyticsEventsMessage.Event _event =
				new AnalyticsEventsMessage.Event();

		}

		private Event() {
		}

		private String _applicationId;
		private String _eventId;
		private Map<String, String> _properties = new HashMap<>();

	}

	private AnalyticsEventsMessage() {
	}

	private String _analyticsKey;
	private Map<String, String> _context = new HashMap<>();
	private final List<Event> _events = new ArrayList<>();
	private String _protocolVersion;
	private long _userId;

}