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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.xml.*;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * Support class for implementing custom {@link org.springframework.beans.factory.xml.NamespaceHandler NamespaceHandlers}.
 * Parsing and decorating of individual {@link Node Nodes} is done via {@link org.springframework.beans.factory.xml.BeanDefinitionParser}
 * and {@link org.springframework.beans.factory.xml.BeanDefinitionDecorator} strategy interfaces, respectively.
 *
 * <p>Provides the {@link #registerBeanDefinitionParser} and {@link #registerBeanDefinitionDecorator}
 * methods for registering a {@link org.springframework.beans.factory.xml.BeanDefinitionParser} or {@link org.springframework.beans.factory.xml.BeanDefinitionDecorator}
 * to handle a specific element.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see #registerBeanDefinitionParser(String, org.springframework.beans.factory.xml.BeanDefinitionParser)
 * @see #registerBeanDefinitionDecorator(String, org.springframework.beans.factory.xml.BeanDefinitionDecorator)
 */
public abstract class NamespaceHandlerSupport implements org.springframework.beans.factory.xml.NamespaceHandler {

	/**
	 * Stores the {@link org.springframework.beans.factory.xml.BeanDefinitionParser} implementations keyed by the
	 * local name of the {@link Element Elements} they handle.
	 */
	private final Map<String, org.springframework.beans.factory.xml.BeanDefinitionParser> parsers =
			new HashMap<>();

	/**
	 * Stores the {@link org.springframework.beans.factory.xml.BeanDefinitionDecorator} implementations keyed by the
	 * local name of the {@link Element Elements} they handle.
	 */
	private final Map<String, org.springframework.beans.factory.xml.BeanDefinitionDecorator> decorators =
			new HashMap<>();

	/**
	 * Stores the {@link org.springframework.beans.factory.xml.BeanDefinitionDecorator} implementations keyed by the local
	 * name of the {@link Attr Attrs} they handle.
	 */
	private final Map<String, org.springframework.beans.factory.xml.BeanDefinitionDecorator> attributeDecorators =
			new HashMap<>();


	/**
	 * Parses the supplied {@link Element} by delegating to the {@link org.springframework.beans.factory.xml.BeanDefinitionParser} that is
	 * registered for that {@link Element}.
	 */
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return findParserForElement(element, parserContext).parse(element, parserContext);
	}

	/**
	 * Locates the {@link org.springframework.beans.factory.xml.BeanDefinitionParser} from the register implementations using
	 * the local name of the supplied {@link Element}.
	 */
	private org.springframework.beans.factory.xml.BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
		String localName = parserContext.getDelegate().getLocalName(element);
		org.springframework.beans.factory.xml.BeanDefinitionParser parser = this.parsers.get(localName);
		if (parser == null) {
			parserContext.getReaderContext().fatal(
					"Cannot locate BeanDefinitionParser for element [" + localName + "]", element);
		}
		return parser;
	}

	/**
	 * Decorates the supplied {@link Node} by delegating to the {@link org.springframework.beans.factory.xml.BeanDefinitionDecorator} that
	 * is registered to handle that {@link Node}.
	 */
	@Override
	public BeanDefinitionHolder decorate(
			Node node, BeanDefinitionHolder definition, ParserContext parserContext) {

		return findDecoratorForNode(node, parserContext).decorate(node, definition, parserContext);
	}

	/**
	 * Locates the {@link org.springframework.beans.factory.xml.BeanDefinitionParser} from the register implementations using
	 * the local name of the supplied {@link Node}. Supports both {@link Element Elements}
	 * and {@link Attr Attrs}.
	 */
	private org.springframework.beans.factory.xml.BeanDefinitionDecorator findDecoratorForNode(Node node, ParserContext parserContext) {
		org.springframework.beans.factory.xml.BeanDefinitionDecorator decorator = null;
		String localName = parserContext.getDelegate().getLocalName(node);
		if (node instanceof Element) {
			decorator = this.decorators.get(localName);
		}
		else if (node instanceof Attr) {
			decorator = this.attributeDecorators.get(localName);
		}
		else {
			parserContext.getReaderContext().fatal(
					"Cannot decorate based on Nodes of type [" + node.getClass().getName() + "]", node);
		}
		if (decorator == null) {
			parserContext.getReaderContext().fatal("Cannot locate BeanDefinitionDecorator for " +
					(node instanceof Element ? "element" : "attribute") + " [" + localName + "]", node);
		}
		return decorator;
	}


	/**
	 * Subclasses can call this to register the supplied {@link org.springframework.beans.factory.xml.BeanDefinitionParser} to
	 * handle the specified element. The element name is the local (non-namespace qualified)
	 * name.
	 */
	protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
		this.parsers.put(elementName, parser);
	}

	/**
	 * Subclasses can call this to register the supplied {@link org.springframework.beans.factory.xml.BeanDefinitionDecorator} to
	 * handle the specified element. The element name is the local (non-namespace qualified)
	 * name.
	 */
	protected final void registerBeanDefinitionDecorator(String elementName, org.springframework.beans.factory.xml.BeanDefinitionDecorator dec) {
		this.decorators.put(elementName, dec);
	}

	/**
	 * Subclasses can call this to register the supplied {@link org.springframework.beans.factory.xml.BeanDefinitionDecorator} to
	 * handle the specified attribute. The attribute name is the local (non-namespace qualified)
	 * name.
	 */
	protected final void registerBeanDefinitionDecoratorForAttribute(String attrName, BeanDefinitionDecorator dec) {
		this.attributeDecorators.put(attrName, dec);
	}

}
