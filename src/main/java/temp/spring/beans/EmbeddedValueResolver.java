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

import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.StringValueResolver;

/**
 * {@link StringValueResolver} adapter for resolving placeholders and
 * expressions against a {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}.
 *
 * <p>Note that this adapter resolves expressions as well, in contrast
 * to the {@link org.springframework.beans.factory.config.ConfigurableBeanFactory#resolveEmbeddedValue} method.
 * The {@link org.springframework.beans.factory.config.BeanExpressionContext} used is for the plain bean factory,
 * with no scope specified for any contextual objects to access.
 *
 * @author Juergen Hoeller
 * @since 4.3
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#resolveEmbeddedValue(String)
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#getBeanExpressionResolver()
 * @see org.springframework.beans.factory.config.BeanExpressionContext
 */
public class EmbeddedValueResolver implements StringValueResolver {

	private final org.springframework.beans.factory.config.BeanExpressionContext exprContext;

	private final org.springframework.beans.factory.config.BeanExpressionResolver exprResolver;


	public EmbeddedValueResolver(ConfigurableBeanFactory beanFactory) {
		this.exprContext = new BeanExpressionContext(beanFactory, null);
		this.exprResolver = beanFactory.getBeanExpressionResolver();
	}


	@Override
	public String resolveStringValue(String strVal) {
		String value = this.exprContext.getBeanFactory().resolveEmbeddedValue(strVal);
		if (this.exprResolver != null && value != null) {
			Object evaluated = this.exprResolver.evaluate(value, this.exprContext);
			value = (evaluated != null ? evaluated.toString() : null);
		}
		return value;
	}

}
