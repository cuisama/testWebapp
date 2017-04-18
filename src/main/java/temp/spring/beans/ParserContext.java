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

package temp.spring.beans;

import java.util.Stack;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.*;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/**
 * Context that gets passed along a bean definition parsing process,
 * encapsulating all relevant configuration as well as state.
 * Nested inside an {@link org.springframework.beans.factory.xml.XmlReaderContext}.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see org.springframework.beans.factory.xml.XmlReaderContext
 * @see org.springframework.beans.factory.xml.BeanDefinitionParserDelegate
 */
public final class ParserContext {

	private final org.springframework.beans.factory.xml.XmlReaderContext readerContext;

	private final org.springframework.beans.factory.xml.BeanDefinitionParserDelegate delegate;

	private BeanDefinition containingBeanDefinition;

	private final Stack<ComponentDefinition> containingComponents = new Stack<>();


	public ParserContext(org.springframework.beans.factory.xml.XmlReaderContext readerContext, org.springframework.beans.factory.xml.BeanDefinitionParserDelegate delegate) {
		this.readerContext = readerContext;
		this.delegate = delegate;
	}

	public ParserContext(org.springframework.beans.factory.xml.XmlReaderContext readerContext, org.springframework.beans.factory.xml.BeanDefinitionParserDelegate delegate,
                         BeanDefinition containingBeanDefinition) {

		this.readerContext = readerContext;
		this.delegate = delegate;
		this.containingBeanDefinition = containingBeanDefinition;
	}


	public final org.springframework.beans.factory.xml.XmlReaderContext getReaderContext() {
		return this.readerContext;
	}

	public final BeanDefinitionRegistry getRegistry() {
		return this.readerContext.getRegistry();
	}

	public final org.springframework.beans.factory.xml.BeanDefinitionParserDelegate getDelegate() {
		return this.delegate;
	}

	public final BeanDefinition getContainingBeanDefinition() {
		return this.containingBeanDefinition;
	}

	public final boolean isNested() {
		return (this.containingBeanDefinition != null);
	}

	public boolean isDefaultLazyInit() {
		return BeanDefinitionParserDelegate.TRUE_VALUE.equals(this.delegate.getDefaults().getLazyInit());
	}

	public Object extractSource(Object sourceCandidate) {
		return this.readerContext.extractSource(sourceCandidate);
	}

	public CompositeComponentDefinition getContainingComponent() {
		return (!this.containingComponents.isEmpty() ?
				(CompositeComponentDefinition) this.containingComponents.lastElement() : null);
	}

	public void pushContainingComponent(CompositeComponentDefinition containingComponent) {
		this.containingComponents.push(containingComponent);
	}

	public CompositeComponentDefinition popContainingComponent() {
		return (CompositeComponentDefinition) this.containingComponents.pop();
	}

	public void popAndRegisterContainingComponent() {
		registerComponent(popContainingComponent());
	}

	public void registerComponent(ComponentDefinition component) {
		CompositeComponentDefinition containingComponent = getContainingComponent();
		if (containingComponent != null) {
			containingComponent.addNestedComponent(component);
		}
		else {
			this.readerContext.fireComponentRegistered(component);
		}
	}

	public void registerBeanComponent(BeanComponentDefinition component) {
		BeanDefinitionReaderUtils.registerBeanDefinition(component, getRegistry());
		registerComponent(component);
	}

}
