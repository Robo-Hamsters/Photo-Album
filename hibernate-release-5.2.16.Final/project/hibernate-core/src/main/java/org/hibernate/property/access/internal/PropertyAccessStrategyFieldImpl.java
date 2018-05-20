/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.property.access.internal;

import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessStrategy;

/**
 * Defines a strategy for accessing property values directly via a field, which may be non-public.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class PropertyAccessStrategyFieldImpl implements PropertyAccessStrategy {
	/**
	 * Singleton access
	 */
	public static final PropertyAccessStrategyFieldImpl INSTANCE = new PropertyAccessStrategyFieldImpl();

	@Override
	public PropertyAccess buildPropertyAccess(Class containerJavaType, String propertyName) {
		return new PropertyAccessFieldImpl( this, containerJavaType, propertyName );
	}
}
