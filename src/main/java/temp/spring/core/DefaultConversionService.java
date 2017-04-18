/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package temp.spring.core;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

/**
 * A specialization of {@link org.springframework.core.convert.support.GenericConversionService} configured by default
 * with converters appropriate for most environments.
 *
 * <p>Designed for direct instantiation but also exposes the static
 * {@link #addDefaultConverters(ConverterRegistry)} utility method for ad-hoc
 * use against any {@code ConverterRegistry} instance.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @since 3.1
 */
public class DefaultConversionService extends GenericConversionService {

	private static volatile org.springframework.core.convert.support.DefaultConversionService sharedInstance;


	/**
	 * Return a shared default {@code ConversionService} instance,
	 * lazily building it once needed.
	 * <p><b>NOTE:</b> We highly recommend constructing individual
	 * {@code ConversionService} instances for customization purposes.
	 * This accessor is only meant as a fallback for code paths which
	 * need simple type coercion but cannot access a longer-lived
	 * {@code ConversionService} instance any other way.
	 * @return the shared {@code ConversionService} instance (never {@code null})
	 * @since 4.3.5
	 */
	public static ConversionService getSharedInstance() {
		if (sharedInstance == null) {
			synchronized (org.springframework.core.convert.support.DefaultConversionService.class) {
				if (sharedInstance == null) {
					sharedInstance = new org.springframework.core.convert.support.DefaultConversionService();
				}
			}
		}
		return sharedInstance;
	}


	/**
	 * Create a new {@code DefaultConversionService} with the set of
	 * {@linkplain org.springframework.core.convert.support.DefaultConversionService#addDefaultConverters(ConverterRegistry) default converters}.
	 */
	public DefaultConversionService() {
		addDefaultConverters(this);
	}


	// static utility methods

	/**
	 * Add converters appropriate for most environments.
	 * @param converterRegistry the registry of converters to add to (must also be castable to ConversionService,
	 * e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a ConversionService
	 */
	public static void addDefaultConverters(ConverterRegistry converterRegistry) {
		addScalarConverters(converterRegistry);
		addCollectionConverters(converterRegistry);

		converterRegistry.addConverter(new org.springframework.core.convert.support.ByteBufferConverter((ConversionService) converterRegistry));
		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToTimeZoneConverter());
		converterRegistry.addConverter(new org.springframework.core.convert.support.ZoneIdToTimeZoneConverter());
		converterRegistry.addConverter(new org.springframework.core.convert.support.ZonedDateTimeToCalendarConverter());

		converterRegistry.addConverter(new org.springframework.core.convert.support.ObjectToObjectConverter());
		converterRegistry.addConverter(new org.springframework.core.convert.support.IdToEntityConverter((ConversionService) converterRegistry));
		converterRegistry.addConverter(new org.springframework.core.convert.support.FallbackObjectToStringConverter());
		converterRegistry.addConverter(new org.springframework.core.convert.support.ObjectToOptionalConverter((ConversionService) converterRegistry));
	}

	/**
	 * Add collection converters.
	 * @param converterRegistry the registry of converters to add to (must also be castable to ConversionService,
	 * e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a ConversionService
	 * @since 4.2.3
	 */
	public static void addCollectionConverters(ConverterRegistry converterRegistry) {
		ConversionService conversionService = (ConversionService) converterRegistry;

		converterRegistry.addConverter(new org.springframework.core.convert.support.ArrayToCollectionConverter(conversionService));
		converterRegistry.addConverter(new org.springframework.core.convert.support.CollectionToArrayConverter(conversionService));

		converterRegistry.addConverter(new org.springframework.core.convert.support.ArrayToArrayConverter(conversionService));
		converterRegistry.addConverter(new org.springframework.core.convert.support.CollectionToCollectionConverter(conversionService));
		converterRegistry.addConverter(new org.springframework.core.convert.support.MapToMapConverter(conversionService));

		converterRegistry.addConverter(new org.springframework.core.convert.support.ArrayToStringConverter(conversionService));
		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToArrayConverter(conversionService));

		converterRegistry.addConverter(new org.springframework.core.convert.support.ArrayToObjectConverter(conversionService));
		converterRegistry.addConverter(new org.springframework.core.convert.support.ObjectToArrayConverter(conversionService));

		converterRegistry.addConverter(new org.springframework.core.convert.support.CollectionToStringConverter(conversionService));
		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToCollectionConverter(conversionService));

		converterRegistry.addConverter(new org.springframework.core.convert.support.CollectionToObjectConverter(conversionService));
		converterRegistry.addConverter(new org.springframework.core.convert.support.ObjectToCollectionConverter(conversionService));

		converterRegistry.addConverter(new org.springframework.core.convert.support.StreamConverter(conversionService));
	}


	// internal helpers

	private static void addScalarConverters(ConverterRegistry converterRegistry) {
		converterRegistry.addConverterFactory(new org.springframework.core.convert.support.NumberToNumberConverterFactory());

		converterRegistry.addConverterFactory(new org.springframework.core.convert.support.StringToNumberConverterFactory());
		converterRegistry.addConverter(Number.class, String.class, new org.springframework.core.convert.support.ObjectToStringConverter());

		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToCharacterConverter());
		converterRegistry.addConverter(Character.class, String.class, new org.springframework.core.convert.support.ObjectToStringConverter());

		converterRegistry.addConverter(new org.springframework.core.convert.support.NumberToCharacterConverter());
		converterRegistry.addConverterFactory(new org.springframework.core.convert.support.CharacterToNumberFactory());

		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToBooleanConverter());
		converterRegistry.addConverter(Boolean.class, String.class, new org.springframework.core.convert.support.ObjectToStringConverter());

		converterRegistry.addConverterFactory(new org.springframework.core.convert.support.StringToEnumConverterFactory());
		converterRegistry.addConverter(new org.springframework.core.convert.support.EnumToStringConverter((ConversionService) converterRegistry));

		converterRegistry.addConverterFactory(new org.springframework.core.convert.support.IntegerToEnumConverterFactory());
		converterRegistry.addConverter(new org.springframework.core.convert.support.EnumToIntegerConverter((ConversionService) converterRegistry));

		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToLocaleConverter());
		converterRegistry.addConverter(Locale.class, String.class, new org.springframework.core.convert.support.ObjectToStringConverter());

		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToCharsetConverter());
		converterRegistry.addConverter(Charset.class, String.class, new org.springframework.core.convert.support.ObjectToStringConverter());

		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToCurrencyConverter());
		converterRegistry.addConverter(Currency.class, String.class, new org.springframework.core.convert.support.ObjectToStringConverter());

		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToPropertiesConverter());
		converterRegistry.addConverter(new org.springframework.core.convert.support.PropertiesToStringConverter());

		converterRegistry.addConverter(new org.springframework.core.convert.support.StringToUUIDConverter());
		converterRegistry.addConverter(UUID.class, String.class, new org.springframework.core.convert.support.ObjectToStringConverter());
	}

}
