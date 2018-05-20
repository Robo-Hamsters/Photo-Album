/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.unionsubclass2;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.TeradataDialect;

import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Gavin King
 */
public class UnionSubclassTest extends BaseCoreFunctionalTestCase {
	protected String[] getMappings() {
		return new String[] { "unionsubclass2/Person.hbm.xml" };
	}

	@Test
	@SkipForDialect(
			value = TeradataDialect.class,
			jiraKey = "HHH-8190",
			comment = "SQL uses Teradata reserved word: title"
	)
	public void testUnionSubclass() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		
		Employee mark = new Employee();
		mark.setName("Mark");
		mark.setTitle("internal sales");
		mark.setSex('M');
		mark.setAddress("buckhead");
		mark.setZip("30305");
		mark.setCountry("USA");
		
		Customer joe = new Customer();
		joe.setName("Joe");
		joe.setAddress("San Francisco");
		joe.setZip("XXXXX");
		joe.setCountry("USA");
		joe.setComments("Very demanding");
		joe.setSex('M');
		joe.setSalesperson(mark);
		
		Person yomomma = new Person();
		yomomma.setName("mum");
		yomomma.setSex('F');
		
		s.save(yomomma);
		s.save(mark);
		s.save(joe);
		
		assertEquals( s.createQuery("from java.io.Serializable").list().size(), 0 );
		
		assertEquals( s.createQuery("from Person").list().size(), 3 );
		assertEquals( s.createQuery("from Person p where p.class = Customer").list().size(), 1 );
		assertEquals( s.createQuery("from Person p where p.class = Person").list().size(), 1 );
		assertEquals( s.createQuery("from Person p where type(p) in :who").setParameter("who", Customer.class).list().size(), 1 );
		assertEquals( s.createQuery("from Person p where type(p) in :who").setParameterList("who", new Class[] {Customer.class, Person.class}).list().size(), 2 );
		s.clear();

		List customers = s.createQuery("from Customer c left join fetch c.salesperson").list();
		for ( Object customer : customers ) {
			Customer c = (Customer) customer;
			assertTrue( Hibernate.isInitialized( c.getSalesperson() ) );
			assertEquals( c.getSalesperson().getName(), "Mark" );
		}
		assertEquals( customers.size(), 1 );
		s.clear();
		
		customers = s.createQuery("from Customer").list();
		for ( Object customer : customers ) {
			Customer c = (Customer) customer;
			assertFalse( Hibernate.isInitialized( c.getSalesperson() ) );
			assertEquals( c.getSalesperson().getName(), "Mark" );
		}
		assertEquals( customers.size(), 1 );
		s.clear();
		

		mark = (Employee) s.get( Employee.class, Long.valueOf( mark.getId() ) );
		joe = (Customer) s.get( Customer.class, Long.valueOf( joe.getId() ) );
		
 		mark.setZip("30306");
		assertEquals( s.createQuery("from Person p where p.address.zip = '30306'").list().size(), 1 );

        s.createCriteria( Person.class ).add(
                Restrictions.in( "address", new Address[] { mark.getAddress(),
                        joe.getAddress() } ) ).list();
		
		s.delete(mark);
		s.delete(joe);
		s.delete(yomomma);
		assertTrue( s.createQuery("from Person").list().isEmpty() );
		t.commit();
		s.close();
	}

	@Test
	@SkipForDialect(
			value = TeradataDialect.class,
			jiraKey = "HHH-8190",
			comment = "SQL uses Teradata reserved word: title"
	)
	public void testQuerySubclassAttribute() {
		if ( getDialect() instanceof HSQLDialect ) {
			return; // TODO : why??
		}
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Person p = new Person();
		p.setName("Emmanuel");
		p.setSex('M');
		s.persist(p);
		Employee q = new Employee();
		q.setName("Steve");
		q.setSex('M');
		q.setTitle("Mr");
		q.setSalary( new BigDecimal(1000) );
		s.persist(q);

		List result = s.createQuery("from Person where salary > 100").list();
		assertEquals( result.size(), 1 );
		assertSame( result.get(0), q );
		
		result = s.createQuery("from Person where salary > 100 or name like 'E%'").list();
		assertEquals( result.size(), 2 );		

		result = s.createCriteria(Person.class)
			.add( Property.forName("salary").gt( new BigDecimal(100) ) )
			.list();
		assertEquals( result.size(), 1 );
		assertSame( result.get(0), q );

		result = s.createQuery("select salary from Person where salary > 100").list();
		assertEquals( result.size(), 1 );
		assertEquals( ( (BigDecimal) result.get(0) ).intValue(), 1000 );
		
		s.delete(p);
		s.delete(q);
		t.commit();
		s.close();
	}

	@Test
	@SkipForDialect(
			value = TeradataDialect.class,
			jiraKey = "HHH-8190",
			comment = "SQL uses Teradata reserved word: title"
	)
	public void testCustomColumnReadAndWrite() {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		final double HEIGHT_INCHES = 73;
		final double HEIGHT_CENTIMETERS = HEIGHT_INCHES * 2.54d;
		Person p = new Person();
		p.setName("Emmanuel");
		p.setSex('M');
		p.setHeightInches(HEIGHT_INCHES);
		s.persist(p);
		final double PASSWORD_EXPIRY_WEEKS = 4;
		final double PASSWORD_EXPIRY_DAYS = PASSWORD_EXPIRY_WEEKS * 7d;
		Employee e = new Employee();
		e.setName("Steve");
		e.setSex('M');
		e.setTitle("Mr");		
		e.setPasswordExpiryDays(PASSWORD_EXPIRY_DAYS);
		s.persist(e);
		s.flush();
		
		// Test value conversion during insert
		// Value returned by Oracle native query is a Types.NUMERIC, which is mapped to a BigDecimalType;
		// Cast returned value to Number then call Number.doubleValue() so it works on all dialects.
		Double heightViaSql =
				( (Number)s.createSQLQuery("select height_centimeters from UPerson where name='Emmanuel'").uniqueResult() ).doubleValue();
		assertEquals(HEIGHT_CENTIMETERS, heightViaSql, 0.01d);
		Double expiryViaSql =
				( (Number)s.createSQLQuery("select pwd_expiry_weeks from UEmployee where person_id=?")
						.setLong(0, e.getId())
						.uniqueResult()
				).doubleValue();
		assertEquals(PASSWORD_EXPIRY_WEEKS, expiryViaSql, 0.01d);
		
		// Test projection
		Double heightViaHql = (Double)s.createQuery("select p.heightInches from Person p where p.name = 'Emmanuel'").uniqueResult();
		assertEquals(HEIGHT_INCHES, heightViaHql, 0.01d);
		Double expiryViaHql = (Double)s.createQuery("select e.passwordExpiryDays from Employee e where e.name = 'Steve'").uniqueResult();
		assertEquals(PASSWORD_EXPIRY_DAYS, expiryViaHql, 0.01d);
		
		// Test restriction and entity load via criteria
		p = (Person)s.createCriteria(Person.class)
			.add(Restrictions.between("heightInches", HEIGHT_INCHES - 0.01d, HEIGHT_INCHES + 0.01d))
			.uniqueResult();
		assertEquals(HEIGHT_INCHES, p.getHeightInches(), 0.01d);
		e = (Employee)s.createCriteria(Employee.class)
			.add(Restrictions.between("passwordExpiryDays", PASSWORD_EXPIRY_DAYS - 0.01d, PASSWORD_EXPIRY_DAYS + 0.01d))
			.uniqueResult();
		assertEquals(PASSWORD_EXPIRY_DAYS, e.getPasswordExpiryDays(), 0.01d);
		
		// Test predicate and entity load via HQL
		p = (Person)s.createQuery("from Person p where p.heightInches between ? and ?")
			.setDouble(0, HEIGHT_INCHES - 0.01d)
			.setDouble(1, HEIGHT_INCHES + 0.01d)
			.uniqueResult();
		assertEquals(HEIGHT_INCHES, p.getHeightInches(), 0.01d);
		e = (Employee)s.createQuery("from Employee e where e.passwordExpiryDays between ? and ?")
			.setDouble(0, PASSWORD_EXPIRY_DAYS - 0.01d)
			.setDouble(1, PASSWORD_EXPIRY_DAYS + 0.01d)
			.uniqueResult();
		assertEquals(PASSWORD_EXPIRY_DAYS, e.getPasswordExpiryDays(), 0.01d);
		
		// Test update
		p.setHeightInches(1);
		e.setPasswordExpiryDays(7);
		s.flush();
		heightViaSql =
				( (Number)s.createSQLQuery("select height_centimeters from UPerson where name='Emmanuel'").uniqueResult() )
						.doubleValue();
		assertEquals(2.54d, heightViaSql, 0.01d);
		expiryViaSql =
				( (Number)s.createSQLQuery("select pwd_expiry_weeks from UEmployee where person_id=?")
						.setLong(0, e.getId())
						.uniqueResult()
				).doubleValue();
		assertEquals(1d, expiryViaSql, 0.01d);
		s.delete(p);
		s.delete(e);
		t.commit();
		s.close();
		
	}
	
	
}

