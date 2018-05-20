/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.metamodel.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.annotations.common.AssertionFailure;

/**
 * Defines commonality for the JPA {@link ManagedType} hierarchy of interfaces.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractManagedType<X> 
		extends AbstractType<X>
		implements ManagedType<X>, Serializable {

	private final  AbstractManagedType<? super X> superType;

	private final Map<String,Attribute<X, ?>> declaredAttributes
			= new HashMap<String, Attribute<X,?>>();
	private final Map<String, SingularAttribute<X, ?>> declaredSingularAttributes
			= new HashMap<String, SingularAttribute<X,?>>();
	private final Map<String, PluralAttribute<X, ?, ?>> declaredPluralAttributes
			= new HashMap<String, PluralAttribute<X,?,?>>();

	protected AbstractManagedType(Class<X> javaType, String typeName, AbstractManagedType<? super X> superType) {
		super( javaType, typeName );
		this.superType = superType;
	}

	protected AbstractManagedType<? super X> getSupertype() {
		return superType;
	}

	private boolean locked;

	public Builder<X> getBuilder() {
		if ( locked ) {
			throw new IllegalStateException( "Type has been locked" );
		}
		return new Builder<X>() {
			@Override
			@SuppressWarnings("unchecked")
			public void addAttribute(Attribute<X,?> attribute) {
				declaredAttributes.put( attribute.getName(), attribute );
				final Bindable.BindableType bindableType = ( ( Bindable ) attribute ).getBindableType();
				switch ( bindableType ) {
					case SINGULAR_ATTRIBUTE : {
						declaredSingularAttributes.put( attribute.getName(), (SingularAttribute<X,?>) attribute );
						break;
					}
					case PLURAL_ATTRIBUTE : {
						declaredPluralAttributes.put(attribute.getName(), (PluralAttribute<X,?,?>) attribute );
						break;
					}
					default : {
						throw new AssertionFailure( "unknown bindable type: " + bindableType );
					}
				}
			}
		};
	}

	public void lock() {
		locked = true;
	}

	public static interface Builder<X> {
		public void addAttribute(Attribute<X,?> attribute);
	}


	@Override
	@SuppressWarnings({ "unchecked" })
	public Set<Attribute<? super X, ?>> getAttributes() {
		HashSet attributes = new HashSet<Attribute<X, ?>>( declaredAttributes.values() );
		if ( getSupertype() != null ) {
			attributes.addAll( getSupertype().getAttributes() );
		}
		return attributes;
	}

	@Override
	public Set<Attribute<X, ?>> getDeclaredAttributes() {
		return new HashSet<Attribute<X, ?>>( declaredAttributes.values() );
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Attribute<? super X, ?> getAttribute(String name) {
		Attribute<? super X, ?> attribute = declaredAttributes.get( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getAttribute( name );
		}
		checkNotNull( "Attribute ", attribute, name );
		return attribute;
	}

	@Override
	public Attribute<X, ?> getDeclaredAttribute(String name) {
		Attribute<X, ?> attr = declaredAttributes.get( name );
		checkNotNull( "Attribute ", attr, name );
		return attr;
	}

	private void checkNotNull(String attributeType, Attribute<?,?> attribute, String name) {

		if ( attribute == null ) {
			throw new IllegalArgumentException(
					String.format(
							"Unable to locate %s with the the given name [%s] on this ManagedType [%s]",
							attributeType,
							name,
							getTypeName()
					)
			);
		}
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Set<SingularAttribute<? super X, ?>> getSingularAttributes() {
		HashSet attributes = new HashSet<SingularAttribute<X, ?>>( declaredSingularAttributes.values() );
		if ( getSupertype() != null ) {
			attributes.addAll( getSupertype().getSingularAttributes() );
		}
		return attributes;
	}

	@Override
	public Set<SingularAttribute<X, ?>> getDeclaredSingularAttributes() {
		return new HashSet<SingularAttribute<X, ?>>( declaredSingularAttributes.values() );
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public SingularAttribute<? super X, ?> getSingularAttribute(String name) {
		SingularAttribute<? super X, ?> attribute = declaredSingularAttributes.get( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getSingularAttribute( name );
		}
		checkNotNull( "SingularAttribute ", attribute, name );
		return attribute;
	}

	@Override
	public SingularAttribute<X, ?> getDeclaredSingularAttribute(String name) {
		final SingularAttribute<X, ?> attr = declaredSingularAttributes.get( name );
		checkNotNull( "SingularAttribute ", attr, name );
		return attr;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<? super X, Y> getSingularAttribute(String name, Class<Y> type) {
		SingularAttribute<? super X, ?> attribute = declaredSingularAttributes.get( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getSingularAttribute( name );
		}
		checkTypeForSingleAttribute( "SingularAttribute ", attribute, name, type );
		return ( SingularAttribute<? super X, Y> ) attribute;
	}

	@Override
	@SuppressWarnings( "unchecked")
	public <Y> SingularAttribute<X, Y> getDeclaredSingularAttribute(String name, Class<Y> javaType) {
		final SingularAttribute<X, ?> attr = declaredSingularAttributes.get( name );
		checkTypeForSingleAttribute( "SingularAttribute ", attr, name, javaType );
		return ( SingularAttribute<X, Y> ) attr;
	}

	private <Y> void checkTypeForSingleAttribute(
			String attributeType,
			SingularAttribute<?,?> attribute,
			String name,
			Class<Y> javaType) {
		if ( attribute == null || ( javaType != null && !attribute.getBindableJavaType().equals( javaType ) ) ) {
			if ( isPrimitiveVariant( attribute, javaType ) ) {
				return;
			}
			throw new IllegalArgumentException(
					attributeType + " named " + name
					+ ( javaType != null ? " and of type " + javaType.getName() : "" )
					+ " is not present"
			);
		}
	}

	@SuppressWarnings({ "SimplifiableIfStatement" })
	protected <Y> boolean isPrimitiveVariant(SingularAttribute<?,?> attribute, Class<Y> javaType) {
		if ( attribute == null ) {
			return false;
		}
		Class declaredType = attribute.getBindableJavaType();

		if ( declaredType.isPrimitive() ) {
			return ( Boolean.class.equals( javaType ) && Boolean.TYPE.equals( declaredType ) )
					|| ( Character.class.equals( javaType ) && Character.TYPE.equals( declaredType ) )
					|| ( Byte.class.equals( javaType ) && Byte.TYPE.equals( declaredType ) )
					|| ( Short.class.equals( javaType ) && Short.TYPE.equals( declaredType ) )
					|| ( Integer.class.equals( javaType ) && Integer.TYPE.equals( declaredType ) )
					|| ( Long.class.equals( javaType ) && Long.TYPE.equals( declaredType ) )
					|| ( Float.class.equals( javaType ) && Float.TYPE.equals( declaredType ) )
					|| ( Double.class.equals( javaType ) && Double.TYPE.equals( declaredType ) );
		}

		if ( javaType.isPrimitive() ) {
			return ( Boolean.class.equals( declaredType ) && Boolean.TYPE.equals( javaType ) )
					|| ( Character.class.equals( declaredType ) && Character.TYPE.equals( javaType ) )
					|| ( Byte.class.equals( declaredType ) && Byte.TYPE.equals( javaType ) )
					|| ( Short.class.equals( declaredType ) && Short.TYPE.equals( javaType ) )
					|| ( Integer.class.equals( declaredType ) && Integer.TYPE.equals( javaType ) )
					|| ( Long.class.equals( declaredType ) && Long.TYPE.equals( javaType ) )
					|| ( Float.class.equals( declaredType ) && Float.TYPE.equals( javaType ) )
					|| ( Double.class.equals( declaredType ) && Double.TYPE.equals( javaType ) );
		}

		return false;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Set<PluralAttribute<? super X, ?, ?>> getPluralAttributes() {
		HashSet attributes = new HashSet<PluralAttribute<? super X, ?, ?>>( declaredPluralAttributes.values() );
		if ( getSupertype() != null ) {
			attributes.addAll( getSupertype().getPluralAttributes() );
		}
		return attributes;
	}

	@Override
	public Set<PluralAttribute<X, ?, ?>> getDeclaredPluralAttributes() {
		return new HashSet<PluralAttribute<X,?,?>>( declaredPluralAttributes.values() );
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public CollectionAttribute<? super X, ?> getCollection(String name) {
		PluralAttribute<? super X, ?, ?> attribute = getPluralAttribute( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		basicCollectionCheck( attribute, name );
		return ( CollectionAttribute<X, ?> ) attribute;
	}

	private PluralAttribute<? super X, ?, ?> getPluralAttribute(String name) {
		return declaredPluralAttributes.get( name );
	}

	private void basicCollectionCheck(PluralAttribute<? super X, ?, ?> attribute, String name) {
		checkNotNull( "CollectionAttribute", attribute, name );
		if ( ! CollectionAttribute.class.isAssignableFrom( attribute.getClass() ) ) {
			throw new IllegalArgumentException( name + " is not a CollectionAttribute: " + attribute.getClass() );
		}
	}

	@Override
	@SuppressWarnings( "unchecked")
	public CollectionAttribute<X, ?> getDeclaredCollection(String name) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		basicCollectionCheck( attribute, name );
		return ( CollectionAttribute<X, ?> ) attribute;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public SetAttribute<? super X, ?> getSet(String name) {
		PluralAttribute<? super X, ?, ?> attribute = getPluralAttribute( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		basicSetCheck( attribute, name );
		return (SetAttribute<? super X, ?>) attribute;
	}

	private void basicSetCheck(PluralAttribute<? super X, ?, ?> attribute, String name) {
		checkNotNull( "SetAttribute", attribute, name );
		if ( ! SetAttribute.class.isAssignableFrom( attribute.getClass() ) ) {
			throw new IllegalArgumentException( name + " is not a SetAttribute: " + attribute.getClass() );
		}
	}

	@Override
	@SuppressWarnings( "unchecked")
	public SetAttribute<X, ?> getDeclaredSet(String name) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		basicSetCheck( attribute, name );
		return ( SetAttribute<X, ?> ) attribute;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public ListAttribute<? super X, ?> getList(String name) {
		PluralAttribute<? super X, ?, ?> attribute = getPluralAttribute( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		basicListCheck( attribute, name );
		return (ListAttribute<? super X, ?>) attribute;
	}

	private void basicListCheck(PluralAttribute<? super X, ?, ?> attribute, String name) {
		checkNotNull( "ListAttribute", attribute, name );
		if ( ! ListAttribute.class.isAssignableFrom( attribute.getClass() ) ) {
			throw new IllegalArgumentException( name + " is not a ListAttribute: " + attribute.getClass() );
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListAttribute<X, ?> getDeclaredList(String name) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		basicListCheck( attribute, name );
		return ( ListAttribute<X, ?> ) attribute;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public MapAttribute<? super X, ?, ?> getMap(String name) {
		PluralAttribute<? super X, ?, ?> attribute = getPluralAttribute( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		basicMapCheck( attribute, name );
		return (MapAttribute<? super X, ?, ?>) attribute;
	}

	private void basicMapCheck(PluralAttribute<? super X, ?, ?> attribute, String name) {
		checkNotNull( "MapAttribute", attribute, name );
		if ( ! MapAttribute.class.isAssignableFrom( attribute.getClass() ) ) {
			throw new IllegalArgumentException( name + " is not a MapAttribute: " + attribute.getClass() );
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public MapAttribute<X, ?, ?> getDeclaredMap(String name) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		basicMapCheck( attribute, name );
		return ( MapAttribute<X,?,?> ) attribute;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <E> CollectionAttribute<? super X, E> getCollection(String name, Class<E> elementType) {
		PluralAttribute<? super X, ?, ?> attribute = declaredPluralAttributes.get( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		checkCollectionElementType( attribute, name, elementType );
		return ( CollectionAttribute<? super X, E> ) attribute;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E> CollectionAttribute<X, E> getDeclaredCollection(String name, Class<E> elementType) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		checkCollectionElementType( attribute, name, elementType );
		return ( CollectionAttribute<X, E> ) attribute;
	}

	private <E> void checkCollectionElementType(PluralAttribute<?,?,?> attribute, String name, Class<E> elementType) {
		checkTypeForPluralAttributes( "CollectionAttribute", attribute, name, elementType, PluralAttribute.CollectionType.COLLECTION );
	}

	private <E> void checkTypeForPluralAttributes(
			String attributeType,
			PluralAttribute<?,?,?> attribute,
			String name,
			Class<E> elementType,
			PluralAttribute.CollectionType collectionType) {
		if ( attribute == null
				|| ( elementType != null && !attribute.getBindableJavaType().equals( elementType ) )
				|| attribute.getCollectionType() != collectionType ) {
			throw new IllegalArgumentException(
					attributeType + " named " + name
					+ ( elementType != null ? " and of element type " + elementType : "" )
					+ " is not present"
			);
		}
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <E> SetAttribute<? super X, E> getSet(String name, Class<E> elementType) {
		PluralAttribute<? super X, ?, ?> attribute = declaredPluralAttributes.get( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		checkSetElementType( attribute, name, elementType );
		return ( SetAttribute<? super X, E> ) attribute;
	}

	private <E> void checkSetElementType(PluralAttribute<? super X, ?, ?> attribute, String name, Class<E> elementType) {
		checkTypeForPluralAttributes( "SetAttribute", attribute, name, elementType, PluralAttribute.CollectionType.SET );
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E> SetAttribute<X, E> getDeclaredSet(String name, Class<E> elementType) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		checkSetElementType( attribute, name, elementType );
		return ( SetAttribute<X, E> ) attribute;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <E> ListAttribute<? super X, E> getList(String name, Class<E> elementType) {
		PluralAttribute<? super X, ?, ?> attribute = declaredPluralAttributes.get( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		checkListElementType( attribute, name, elementType );
		return ( ListAttribute<? super X, E> ) attribute;
	}

	private <E> void checkListElementType(PluralAttribute<? super X, ?, ?> attribute, String name, Class<E> elementType) {
		checkTypeForPluralAttributes( "ListAttribute", attribute, name, elementType, PluralAttribute.CollectionType.LIST );
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E> ListAttribute<X, E> getDeclaredList(String name, Class<E> elementType) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		checkListElementType( attribute, name, elementType );
		return ( ListAttribute<X, E> ) attribute;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <K, V> MapAttribute<? super X, K, V> getMap(String name, Class<K> keyType, Class<V> valueType) {
		PluralAttribute<? super X, ?, ?> attribute = getPluralAttribute( name );
		if ( attribute == null && getSupertype() != null ) {
			attribute = getSupertype().getPluralAttribute( name );
		}
		checkMapValueType( attribute, name, valueType );
		final MapAttribute<? super X, K, V> mapAttribute = ( MapAttribute<? super X, K, V> ) attribute;
		checkMapKeyType( mapAttribute, name, keyType );
		return mapAttribute;
	}

	private <V> void checkMapValueType(PluralAttribute<? super X, ?, ?> attribute, String name, Class<V> valueType) {
		checkTypeForPluralAttributes( "MapAttribute", attribute, name, valueType, PluralAttribute.CollectionType.MAP);
	}

	private <K,V> void checkMapKeyType(MapAttribute<? super X, K, V> mapAttribute, String name, Class<K> keyType) {
		if ( mapAttribute.getKeyJavaType() != keyType ) {
			throw new IllegalArgumentException( "MapAttribute named " + name + " does not support a key of type " + keyType );
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K, V> MapAttribute<X, K, V> getDeclaredMap(String name, Class<K> keyType, Class<V> valueType) {
		final PluralAttribute<X,?,?> attribute = declaredPluralAttributes.get( name );
		checkMapValueType( attribute, name, valueType );
		final MapAttribute<X, K, V> mapAttribute = ( MapAttribute<X, K, V> ) attribute;
		checkMapKeyType( mapAttribute, name, keyType );
		return mapAttribute;
	}
}
