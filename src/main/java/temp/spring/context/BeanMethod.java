/*
 * Copyright 2002-2011 the original author or authors.
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

package temp.spring.context;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClass;
import org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader;
import org.springframework.context.annotation.ConfigurationClassParser;
import org.springframework.core.type.MethodMetadata;

/**
 * Represents a {@link org.springframework.context.annotation.Configuration} class method marked with the
 * {@link org.springframework.context.annotation.Bean} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.0
 * @see org.springframework.context.annotation.ConfigurationClass
 * @see org.springframework.context.annotation.ConfigurationClassParser
 * @see org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader
 */
final class BeanMethod extends ConfigurationMethod {

	public BeanMethod(MethodMetadata metadata, org.springframework.context.annotation.ConfigurationClass configurationClass) {
		super(metadata, configurationClass);
	}

	@Override
	public void validate(ProblemReporter problemReporter) {
		if (getMetadata().isStatic()) {
			// static @Bean methods have no constraints to validate -> return immediately
			return;
		}

		if (this.configurationClass.getMetadata().isAnnotated(Configuration.class.getName())) {
			if (!getMetadata().isOverridable()) {
				// instance @Bean methods within @Configuration classes must be overridable to accommodate CGLIB
				problemReporter.error(new NonOverridableMethodError());
			}
		}
	}


	private class NonOverridableMethodError extends Problem {

		public NonOverridableMethodError() {
			super(String.format("@Bean method '%s' must not be private or final; change the method's modifiers to continue",
					getMetadata().getMethodName()), getResourceLocation());
		}
	}
}
