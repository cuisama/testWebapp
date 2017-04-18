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

import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.support.MethodOverride;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Set of method overrides, determining which, if any, methods on a
 * managed object the Spring IoC container will override at runtime.
 *
 * <p>The currently supported {@link org.springframework.beans.factory.support.MethodOverride} variants are
 * {@link org.springframework.beans.factory.support.LookupOverride} and {@link ReplaceOverride}.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 1.1
 * @see org.springframework.beans.factory.support.MethodOverride
 */
public class MethodOverrides {

	private final Set<org.springframework.beans.factory.support.MethodOverride> overrides =
			Collections.synchronizedSet(new LinkedHashSet<org.springframework.beans.factory.support.MethodOverride>(0));

	private volatile boolean modified = false;


	/**
	 * Create new MethodOverrides.
	 */
	public MethodOverrides() {
	}

	/**
	 * Deep copy constructor.
	 */
	public MethodOverrides(MethodOverrides other) {
		addOverrides(other);
	}


	/**
	 * Copy all given method overrides into this object.
	 */
	public void addOverrides(MethodOverrides other) {
		if (other != null) {
			this.modified = true;
			this.overrides.addAll(other.overrides);
		}
	}

	/**
	 * Add the given method override.
	 */
	public void addOverride(org.springframework.beans.factory.support.MethodOverride override) {
		this.modified = true;
		this.overrides.add(override);
	}

	/**
	 * Return all method overrides contained by this object.
	 * @return Set of MethodOverride objects
	 * @see org.springframework.beans.factory.support.MethodOverride
	 */
	public Set<org.springframework.beans.factory.support.MethodOverride> getOverrides() {
		this.modified = true;
		return this.overrides;
	}

	/**
	 * Return whether the set of method overrides is empty.
	 */
	public boolean isEmpty() {
		return (!this.modified || this.overrides.isEmpty());
	}

	/**
	 * Return the override for the given method, if any.
	 * @param method method to check for overrides for
	 * @return the method override, or {@code null} if none
	 */
	public org.springframework.beans.factory.support.MethodOverride getOverride(Method method) {
		if (!this.modified) {
			return null;
		}
		synchronized (this.overrides) {
			org.springframework.beans.factory.support.MethodOverride match = null;
			for (MethodOverride candidate : this.overrides) {
				if (candidate.matches(method)) {
					match = candidate;
				}
			}
			return match;
		}
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MethodOverrides)) {
			return false;
		}
		MethodOverrides that = (MethodOverrides) other;
		return this.overrides.equals(that.overrides);

	}

	@Override
	public int hashCode() {
		return this.overrides.hashCode();
	}

}
