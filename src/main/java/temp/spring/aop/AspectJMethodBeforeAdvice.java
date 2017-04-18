/*
 * Copyright 2002-2015 the original author or authors.
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

package temp.spring.aop;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * Spring AOP advice that wraps an AspectJ before method.
 *
 * @author Rod Johnson
 * @author Adrian Colyer
 * @since 2.0
 */
@SuppressWarnings("serial")
public class AspectJMethodBeforeAdvice extends temp.spring.aop.AbstractAspectJAdvice implements MethodBeforeAdvice, Serializable {

	public AspectJMethodBeforeAdvice(
            Method aspectJBeforeAdviceMethod, AspectJExpressionPointcut pointcut, org.springframework.aop.aspectj.AspectInstanceFactory aif) {

		super(aspectJBeforeAdviceMethod, pointcut, aif);
	}


	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		invokeAdviceMethod(getJoinPointMatch(), null, null);
	}

	@Override
	public boolean isBeforeAdvice() {
		return true;
	}

	@Override
	public boolean isAfterAdvice() {
		return false;
	}

}
