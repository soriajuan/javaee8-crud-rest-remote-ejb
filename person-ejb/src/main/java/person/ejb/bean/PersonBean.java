package person.ejb.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import person.common.exception.EntityNotFoundException;
import person.common.pojo.Person;
import person.common.service.PersonService;

@Stateless
@Remote(PersonService.class)
public class PersonBean implements PersonService, Serializable {

	private static final long serialVersionUID = 325145112790780867L;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private PersonMapper mapper;

	@Override
	public List<Person> findAll() {
		return em.createQuery("from PersonH2", PersonH2.class).getResultList().stream().map(e -> mapper.toPerson(e))
				.collect(Collectors.toList());
	}

	@Override
	public Person findById(int id) {
		PersonH2 entity = em.find(PersonH2.class, id);
		if (Objects.isNull(entity)) {
			throw new EntityNotFoundException(String.format("person not found under id %d", id));
		}
		return mapper.toPerson(entity);
	}

	@Override
	public Person insert(Person p) {
		if (Objects.isNull(p)) {
			throw new IllegalArgumentException("person to be inserted cannot be null");
		}
		PersonH2 entity = mapper.toPersonH2(p);
		em.persist(entity);
		return mapper.toPerson(entity);
	}

	@Override
	public Person update(int id, Person person) {
		if (Objects.isNull(person)) {
			throw new IllegalArgumentException("person to be updated cannot be null");
		}

		PersonH2 entity = em.find(PersonH2.class, id);
		if (Objects.isNull(entity)) {
			throw new EntityNotFoundException(String.format("person not found under id %d", id));
		}

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());

		return mapper.toPerson(em.merge(entity));
	}

	@Override
	public void deleteById(int id) {
		PersonH2 entity = em.find(PersonH2.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

}
