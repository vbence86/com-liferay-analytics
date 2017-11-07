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

package com.liferay.analytics.model;

import java.util.List;
import java.util.Map;

/**
 * @author Eduardo Garcia
 * @author Marcellus Tavares
 */
public interface AnalyticsEventsMessage
	<T extends AnalyticsEventsMessage.Event> {

	public String getAnalyticsKey();

	public Map<String, String> getContext();

	public List<T> getEvents();

	public String getProtocolVersion();

	public String getUserId();

	public interface Event {

		public String getApplicationId();

		public String getEventId();

		public Map<String, String> getProperties();

	}

}