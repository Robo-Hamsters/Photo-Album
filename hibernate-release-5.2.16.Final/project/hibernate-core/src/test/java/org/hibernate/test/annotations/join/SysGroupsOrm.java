/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.annotations.join;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "SYS_GROUPS" )
public class SysGroupsOrm {

	private long groupId;

	@Id
	@GeneratedValue
	@Column( name = "GROUPID" )
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId( long groupId ) {
		this.groupId = groupId;
	}

}
