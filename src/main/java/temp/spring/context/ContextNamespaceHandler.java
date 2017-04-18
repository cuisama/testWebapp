/*
 * Copyright 2002-2012 the original author or authors.
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

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;
import org.springframework.context.config.*;
import org.springframework.context.config.LoadTimeWeaverBeanDefinitionParser;
import org.springframework.context.config.MBeanExportBeanDefinitionParser;
import org.springframework.context.config.MBeanServerBeanDefinitionParser;
import org.springframework.context.config.SpringConfiguredBeanDefinitionParser;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '{@code context}' namespace.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 */
public class ContextNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("property-placeholder", new org.springframework.context.config.PropertyPlaceholderBeanDefinitionParser());
		registerBeanDefinitionParser("property-override", new PropertyOverrideBeanDefinitionParser());
		registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
		registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
		registerBeanDefinitionParser("load-time-weaver", new org.springframework.context.config.LoadTimeWeaverBeanDefinitionParser());
		registerBeanDefinitionParser("spring-configured", new org.springframework.context.config.SpringConfiguredBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-export", new org.springframework.context.config.MBeanExportBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-server", new org.springframework.context.config.MBeanServerBeanDefinitionParser());
	}

}
