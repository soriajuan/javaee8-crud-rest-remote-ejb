package person.web.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import person.common.pojo.Person;

public class PersonMapperTest {

	final Integer id = 1;
	final String firstName = "John";
	final String lastName = "Doe";

	PersonMapper mapper = Mappers.getMapper(PersonMapper.class);

	@Test
	public void fromPersonPostRequestPayloadtoPerson() {
		PersonPostRequestPayload personPostRequestPayload = new PersonPostRequestPayload(firstName, lastName);
		Person person = Person.builder().firstName(firstName).lastName(lastName).build();
		assertEquals(person, mapper.toPerson(personPostRequestPayload));
	}

	@Test
	public void fromPersonPatchRequestPayloadtoPerson() {
		PersonPatchRequestPayload personPatchRequestPayload = new PersonPatchRequestPayload(firstName, lastName);
		Person person = Person.builder().firstName(firstName).lastName(lastName).build();
		assertEquals(person, mapper.toPerson(personPatchRequestPayload));
	}
	
	@Test
	public void fromPersonToPersonGetResponsePayload() {
		Person person = Person.builder().id(id).firstName(firstName).lastName(lastName).build();
		PersonGetResponsePayload personGetResponsePayload = new PersonGetResponsePayload(id, firstName, lastName);
		assertEquals(personGetResponsePayload, mapper.toPersonGetResponsePayload(person));
	}

}
