package person.ejb.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import person.common.exception.EntityNotFoundException;
import person.common.pojo.Person;

@ExtendWith(MockitoExtension.class)
public class PersonBeanTest {

	@Mock
	EntityManager em;

	@Mock
	PersonMapper mapper;

	@InjectMocks
	PersonBean personBean;

	@Test
	public void findAll(@Mock TypedQuery<PersonH2> typedQuery) {
		when(em.createQuery("from PersonH2", PersonH2.class)).thenReturn(typedQuery);

		when(typedQuery.getResultList()).thenReturn(PersonDataMock.allPersonEntities());

		when(mapper.toPerson(any(PersonH2.class))).thenAnswer(new Answer<Person>() {
			@Override
			public Person answer(InvocationOnMock invocation) throws Throwable {
				PersonH2 entity = invocation.getArgument(0, PersonH2.class);
				return Person.builder().id(entity.getId()).firstName(entity.getFirstName())
						.lastName(entity.getLastName()).build();
			}
		});

		List<Person> actual = personBean.findAll();

		assertEquals(PersonDataMock.allPersons(), actual);

		verify(em).createQuery("from PersonH2", PersonH2.class);
		verify(typedQuery).getResultList();
		verify(mapper, times(3)).toPerson(any(PersonH2.class));
		verifyNoMoreInteractions(em, typedQuery, mapper);
	}

	@Test
	public void findByIdEntityNotFound() {
		int id = 0;
		when(em.find(PersonH2.class, id)).thenReturn(null);

		EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
			personBean.findById(id);
		});

		assertEquals("person not found under id " + id, exception.getMessage());

		verify(em).find(PersonH2.class, id);
		verifyNoMoreInteractions(em);
		verifyNoInteractions(mapper);
	}

	@Test
	public void findById() {
		int id = 1;

		when(em.find(PersonH2.class, id)).thenReturn(PersonDataMock.nikolaTeslaH2);
		when(mapper.toPerson(any(PersonH2.class))).thenAnswer(new Answer<Person>() {
			@Override
			public Person answer(InvocationOnMock invocation) throws Throwable {
				PersonH2 entity = invocation.getArgument(0, PersonH2.class);
				return Person.builder().id(entity.getId()).firstName(entity.getFirstName())
						.lastName(entity.getLastName()).build();
			}
		});

		Person actual = personBean.findById(id);

		assertEquals(PersonDataMock.nikolaTesla, actual);

		verify(em).find(PersonH2.class, id);
		verify(mapper).toPerson(any(PersonH2.class));
		verifyNoMoreInteractions(em, mapper);
	}

	@Test
	public void insertFailWithNullParameter() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			personBean.insert(null);
		});
		assertEquals("person to be inserted cannot be null", exception.getMessage());
	}

	@Test
	public void insert() {
		Integer id = 1;

		Person toInsert = PersonDataMock.nikolaTesla.toBuilder().id(null).build();

		when(mapper.toPersonH2(toInsert)).thenAnswer(new Answer<PersonH2>() {
			@Override
			public PersonH2 answer(InvocationOnMock invocation) throws Throwable {
				Person p = invocation.getArgument(0, Person.class);
				return new PersonH2(null, p.getFirstName(), p.getLastName());
			}
		});

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				PersonH2 p = invocation.getArgument(0, PersonH2.class);
				p.setId(id);
				return null;
			}
		}).when(em).persist(any(PersonH2.class));

		when(mapper.toPerson(any(PersonH2.class))).thenAnswer(new Answer<Person>() {
			@Override
			public Person answer(InvocationOnMock invocation) throws Throwable {
				PersonH2 p = invocation.getArgument(0, PersonH2.class);
				return Person.builder().id(p.getId()).firstName(p.getFirstName()).lastName(p.getLastName()).build();
			}
		});

		Person inserted = personBean.insert(toInsert);

		assertEquals(PersonDataMock.nikolaTesla, inserted);

		verify(mapper).toPersonH2(toInsert);
		verify(em).persist(any(PersonH2.class));
		verify(mapper).toPerson(any(PersonH2.class));
		verifyNoMoreInteractions(mapper, em);
	}

	@Test
	public void updateNullPersonParameter() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			personBean.update(0, null);
		});
		assertEquals("person to be updated cannot be null", exception.getMessage());
	}

	@Test
	public void updateEntityNotFound() {
		Integer id = 1;

		when(em.find(PersonH2.class, id)).thenReturn(null);

		EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
			Person toUpdate = Person.builder().firstName("UPDATE THIS").lastName("UPDATE THIS").build();
			personBean.update(id, toUpdate);
		});
		assertEquals("person not found under id " + id, exception.getMessage());

		verify(em).find(PersonH2.class, id);
		verifyNoMoreInteractions(em);
	}

	@Test
	public void update() {
		Integer id = 1;

		PersonH2 nikolaTeslaH2 = new PersonH2(id, "Nikola", "Tesla");

		when(em.find(PersonH2.class, id)).thenReturn(nikolaTeslaH2);

		PersonH2 nikolaTeslaUpdatedH2 = new PersonH2(id, "Nikola-UPDATED", "Tesla-UPDATED");
		when(em.merge(any(PersonH2.class))).thenReturn(nikolaTeslaUpdatedH2);

		when(mapper.toPerson(any(PersonH2.class))).thenAnswer(new Answer<Person>() {
			@Override
			public Person answer(InvocationOnMock invocation) throws Throwable {
				PersonH2 p = invocation.getArgument(0, PersonH2.class);
				return Person.builder().id(p.getId()).firstName(p.getFirstName()).lastName(p.getLastName()).build();
			}
		});

		Person toUpdate = Person.builder().firstName("Nikola-UPDATED").lastName("Tesla-UPDATED").build();

		Person updated = personBean.update(id, toUpdate);

		Person expected = toUpdate.toBuilder().id(id).build();
		assertEquals(expected, updated);

		verify(em).find(PersonH2.class, id);
		verify(em).merge(any(PersonH2.class));
		verify(mapper).toPerson(any(PersonH2.class));
		verifyNoMoreInteractions(em, mapper);
	}

	@Test
	public void deleteById() {
		Integer id = 1;

		when(em.find(PersonH2.class, id)).thenReturn(PersonDataMock.nikolaTeslaH2);
		doNothing().when(em).remove(any(PersonH2.class));

		personBean.deleteById(id);

		verify(em).find(PersonH2.class, id);
		verify(em).remove(any(PersonH2.class));
		verifyNoMoreInteractions(em);
	}

}
