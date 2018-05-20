/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.test.integration.interfaces.hbm.allAudited;

import org.hibernate.envers.Audited;

/**
 * @author Hern�n Chanfreau
 */
@Audited
public class AuditedImplementor implements SimpleInterface {
	private long id;
	private String data;
	private String auditedImplementorData;

	protected AuditedImplementor() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getAuditedImplementorData() {
		return auditedImplementorData;
	}

	public void setAuditedImplementorData(String implementorData) {
		this.auditedImplementorData = implementorData;
	}

}
