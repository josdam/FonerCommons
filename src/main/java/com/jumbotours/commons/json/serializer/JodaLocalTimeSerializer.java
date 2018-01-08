package com.jumbotours.commons.json.serializer;

/*
 * #%L
 * Hotel Content Model
 * %%
 * Copyright (C) 2015 - 2016 HOTELBEDS TECHNOLOGY, S.L.U.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

/**
 * The Class JodaLocalTimeSerializer.
 */
public class JodaLocalTimeSerializer extends JsonSerializer<LocalTime> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator,
	 * com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(final LocalTime date, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
		final String dateString = date.toString(DateTimeFormat.forPattern("HH:mm:ss"));
		generator.writeString(dateString);
	}
}
