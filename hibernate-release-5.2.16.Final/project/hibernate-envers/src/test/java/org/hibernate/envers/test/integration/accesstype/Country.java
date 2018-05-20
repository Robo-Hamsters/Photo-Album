/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.test.integration.accesstype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Country {
	@Id
	@Column(length = 4)
	private Integer code;

	@Column(length = 40)
	private String name;

	/**
	 * Default constructor for persistence provider.
	 */
	@SuppressWarnings({"UnusedDeclaration"})
	private Country() {
	}

	private Country(Integer code, String naam) {
		this.code = code;
		this.name = naam;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static Country of(Integer code, String name) {
		return new Country( code, name );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		Country other = (Country) obj;
		if ( code == null ) {
			if ( other.code != null ) {
				return false;
			}
		}
		else if ( !code.equals( other.code ) ) {
			return false;
		}
		if ( name == null ) {
			if ( other.name != null ) {
				return false;
			}
		}
		else if ( !name.equals( other.name ) ) {
			return false;
		}
		return true;
	}
}
