package person.it.ejb;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.DBUnitExtension;

import person.common.exception.EntityNotFoundException;
import person.common.pojo.Person;
import person.common.service.PersonService;

@ExtendWith(DBUnitExtension.class)
public class PersonEjbIT {

	static PersonService personService;

	@BeforeAll
	public static void setup() throws NamingException {
		Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		Context ctx = new InitialContext(jndiProperties);

		personService = (PersonService) ctx
				.lookup("ejb:person-backend-ear/person-ejb/PersonBean!person.common.service.PersonService");
	}

	@Test
	@DataSet(value = "yml/everybody.yml")
	public void findAll() {
		List<Person> findAll = personService.findAll();

		List<Person> expected = Arrays.asList(Person.builder().id(1).firstName("Nikola").lastName("Tesla").build(),
				Person.builder().id(2).firstName("Albert").lastName("Einstein").build(),
				Person.builder().id(3).firstName("Carl").lastName("Sagan").build());

		assertEquals(expected, findAll);
	}

	@Test
	@DataSet(value = "yml/empty.yml")
	public void findByIdEntityNotFound() {
		Integer id = 1;
		EJBException exception = Assertions.assertThrows(EJBException.class, () -> {
			personService.findById(id);
		});
		assertThat(exception.getCause(), instanceOf(EntityNotFoundException.class));
		assertEquals("person not found under id " + id, exception.getCause().getMessage());
	}

	@Test
	@DataSet(value = "yml/nikolaTesla.yml")
	public void findById() {
		Person expected = Person.builder().id(1).firstName("Nikola").lastName("Tesla").build();
		Person actual = personService.findById(1);

		assertEquals(expected, actual);
	}

	@Test
	public void insertPersonCannotBeNull() {
		EJBException exception = Assertions.assertThrows(EJBException.class, () -> {
			personService.insert(null);
		});
		assertThat(exception.getCause(), instanceOf(IllegalArgumentException.class));
		assertEquals("person to be inserted cannot be null", exception.getCause().getMessage());
	}

	@Test
	@ExpectedDataSet(value = "yml/nikolaTesla.yml")
	public void insert() {
		Person expected = Person.builder().id(1).firstName("Nikola").lastName("Tesla").build();
		Person actual = personService.insert(Person.builder().firstName("Nikola").lastName("Tesla").build());
		assertEquals(expected, actual);
	}

	@Test
	public void updatePersonCannotBeNull() {
		EJBException exception = Assertions.assertThrows(EJBException.class, () -> {
			personService.update(0, null);
		});
		assertThat(exception.getCause(), instanceOf(IllegalArgumentException.class));
		assertEquals("person to be updated cannot be null", exception.getCause().getMessage());
	}

	@Test
	@DataSet(value = "yml/empty.yml")
	public void updateEntityNotFound() {
		Integer id = 1;
		EJBException exception = Assertions.assertThrows(EJBException.class, () -> {
			personService.update(id, Person.builder().firstName("Nikola").lastName("Tesla").build());
		});
		assertThat(exception.getCause(), instanceOf(EntityNotFoundException.class));
		assertEquals("person not found under id " + id, exception.getCause().getMessage());
	}

	@Test
	@DataSet(value = "yml/nikolaTesla.yml")
	@ExpectedDataSet(value = "yml/nikolaTesla-updated.yml")
	public void update() {
		Person expected = Person.builder().id(1).firstName("Nikola-updated").lastName("Tesla-updated").build();
		Person actual = personService.update(1,
				Person.builder().firstName("Nikola-updated").lastName("Tesla-updated").build());
		assertEquals(expected, actual);
	}

	@Test
	@DataSet(value = "yml/nikolaTesla.yml")
	@ExpectedDataSet(value = "yml/empty.yml")
	public void deleteById() {
		personService.deleteById(1);
	}

}
