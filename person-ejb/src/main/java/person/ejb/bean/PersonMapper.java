package person.ejb.bean;

import org.mapstruct.Mapper;

import person.common.pojo.Person;

@Mapper(componentModel = "cdi")
interface PersonMapper {

	Person toPerson(PersonH2 p);

	PersonH2 toPersonH2(Person p);

}
