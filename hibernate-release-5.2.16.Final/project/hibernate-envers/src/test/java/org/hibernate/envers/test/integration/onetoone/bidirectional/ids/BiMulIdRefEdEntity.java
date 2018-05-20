/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.test.integration.onetoone.bidirectional.ids;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.test.entities.ids.MulId;

/**
 * @author Adam Warski (adam at warski dot org)
 */
@Entity
@IdClass(MulId.class)
public class BiMulIdRefEdEntity {
	@Id
	private Integer id1;

	@Id
	private Integer id2;

	@Audited
	private String data;

	@Audited
	@OneToOne(mappedBy = "reference")
	private BiMulIdRefIngEntity referencing;

	public BiMulIdRefEdEntity() {
	}

	public BiMulIdRefEdEntity(Integer id1, Integer id2, String data) {
		this.id1 = id1;
		this.id2 = id2;
		this.data = data;
	}

	public Integer getId1() {
		return id1;
	}

	public void setId1(Integer id1) {
		this.id1 = id1;
	}

	public Integer getId2() {
		return id2;
	}

	public void setId2(Integer id2) {
		this.id2 = id2;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public BiMulIdRefIngEntity getReferencing() {
		return referencing;
	}

	public void setReferencing(BiMulIdRefIngEntity referencing) {
		this.referencing = referencing;
	}

	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( !(o instanceof BiMulIdRefEdEntity) ) {
			return false;
		}

		BiMulIdRefEdEntity that = (BiMulIdRefEdEntity) o;

		if ( data != null ? !data.equals( that.data ) : that.data != null ) {
			return false;
		}
		if ( id1 != null ? !id1.equals( that.id1 ) : that.id1 != null ) {
			return false;
		}
		if ( id2 != null ? !id2.equals( that.id2 ) : that.id2 != null ) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result;
		result = (id1 != null ? id1.hashCode() : 0);
		result = 31 * result + (id2 != null ? id2.hashCode() : 0);
		result = 31 * result + (data != null ? data.hashCode() : 0);
		return result;
	}
}