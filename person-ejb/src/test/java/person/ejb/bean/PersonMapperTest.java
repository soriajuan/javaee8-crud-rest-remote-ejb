package person.ejb.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import person.common.pojo.Person;

public class PersonMapperTest {

	final Integer id = 1;
	final String firstName = "John";
	final String lastName = "Doe";

	PersonMapper mapper = Mappers.getMapper(PersonMapper.class);

	PersonH2 entity = new PersonH2(id, firstName, lastName);
	Person person = Person.builder().id(id).firstName(firstName).lastName(lastName).build();

	@Test
	public void toPersonTest() {
		assertEquals(person, mapper.toPerson(entity));
	}

	@Test
	public void toPersonH2Test() {
		assertEquals(entity, mapper.toPersonH2(person));
	}

}
