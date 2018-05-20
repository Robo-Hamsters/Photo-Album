/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.mapping;

import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.mapping.Table;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseNonConfigCoreFunctionalTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Column aliases utilize {@link Table#getUniqueInteger()} for naming.  The
 * unique integer used to be statically generated by the Table class, meaning
 * it was dependent on mapping order.  HHH-2448 made the alias names
 * deterministic by having Configuration determine the unique integers on its
 * second pass over the Tables tree map.  AliasTest and
 * {@link MappingReorderedAliasTest} ensure that the unique integers are the
 * same, regardless of mapping ordering.
 * 
 * @author Brett Meyer
 */
public class AliasTest extends BaseNonConfigCoreFunctionalTestCase {
	
	/**
	 * Column aliases utilize {@link Table#getUniqueInteger()} for naming.  The unique integer used to be statically
	 * generated by the Table class, meaning it was dependent on mapping order.  HHH-2448 made the alias names
	 * deterministic by having Configuration determine the unique integers on its second pass over the Tables tree map.
	 * AliasTest and {@link MappingReorderedAliasTest} ensure that the unique integers are the same, regardless of
	 * mapping ordering.
	 */
	@Test
	@TestForIssue( jiraKey = "HHH-2448" )
	public void testAliasOrdering() {
		Iterator<Table> tables = metadata().collectTableMappings().iterator();
		Table table1 = null;
		Table table2 = null;
		while ( tables.hasNext() ) {
			Table table = tables.next();
			if ( table.getName().equals( "Table1" ) ) {
				table1 = table;
			}
			else if ( table.getName().equals( "Table2" ) ) {
				table2 = table;
			}
		}
		
		assertTrue( table1.getUniqueInteger() < table2.getUniqueInteger() );
	}
	
	@Test
	@TestForIssue( jiraKey = "HHH-8371" )
	public final void testUnderscoreInColumnName() throws Throwable {
		final Session s = openSession();
		s.getTransaction().begin();
		
		UserEntity user = new UserEntity();
		user.setName( "foo" );
		s.persist(user);
		final ConfEntity conf =  new ConfEntity();
		conf.setConfKey("counter");
		conf.setConfValue("3");
		final UserConfEntity uc = new UserConfEntity();
		uc.setUser(user);
		uc.setConf(conf);
		conf.getUserConf().add(uc);
		s.persist(conf);

		s.getTransaction().commit();
		s.clear();
		
		s.getTransaction().begin();
		user = (UserEntity) s.get(UserEntity.class, user.getId());

		try {
			s.flush();
		}
		catch ( HibernateException e ) {
			// original issue from HHH-8371
			fail( "The explicit column name's underscore(s) were not considered during alias creation." );
		}
		
		assertNotNull( user );
		assertEquals( user.getName(), "foo" );

		s.getTransaction().commit();
		s.close();
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] { Table1.class, Table2.class, ConfEntity.class, UserConfEntity.class, UserEntity.class };
	}
}
