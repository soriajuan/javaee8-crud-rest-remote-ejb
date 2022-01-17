package person.common.service;

import java.util.List;

import person.common.pojo.Person;

public interface PersonService {

	List<Person> findAll();

	Person findById(int id);

	Person insert(Person p);

	Person update(int id, Person p);

	void deleteById(int id);

}
