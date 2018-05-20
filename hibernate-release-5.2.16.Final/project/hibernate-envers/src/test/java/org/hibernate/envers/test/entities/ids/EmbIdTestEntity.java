/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.test.entities.ids;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@Table(name = "EmbIdEnt")
public class EmbIdTestEntity {
	@EmbeddedId
	private EmbId id;

	@Audited
	private String str1;

	public EmbIdTestEntity() {
	}

	public EmbIdTestEntity(String str1) {
		this.str1 = str1;
	}

	public EmbIdTestEntity(EmbId id, String str1) {
		this.id = id;
		this.str1 = str1;
	}

	public EmbId getId() {
		return id;
	}

	public void setId(EmbId id) {
		this.id = id;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof EmbIdTestEntity) ) {
			return false;
		}

		EmbIdTestEntity that = (EmbIdTestEntity) o;

		if ( id != null ? !id.equals( that.id ) : that.id != null ) {
			return false;
		}
		if ( str1 != null ? !str1.equals( that.str1 ) : that.str1 != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (str1 != null ? str1.hashCode() : 0);
		return result;
	}
}
