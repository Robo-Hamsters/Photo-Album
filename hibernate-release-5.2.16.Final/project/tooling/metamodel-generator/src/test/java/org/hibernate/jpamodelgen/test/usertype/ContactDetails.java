/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.jpamodelgen.test.usertype;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

/**
 * @author Hardy Ferentschik
 */
@Entity
public class ContactDetails {
	@Id
	private long id;

	@Type(type = "foo")
	private PhoneNumber phoneNumber;
}


