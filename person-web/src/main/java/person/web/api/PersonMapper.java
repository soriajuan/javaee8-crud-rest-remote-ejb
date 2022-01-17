package person.web.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import person.common.pojo.Person;

@Mapper(componentModel = "cdi")
interface PersonMapper {

	@Mapping(target = "id", ignore = true)
	Person toPerson(PersonPostRequestPayload p);
	
	@Mapping(target = "id", ignore = true)
	Person toPerson(PersonPatchRequestPayload p);

	PersonGetResponsePayload toPersonGetResponsePayload(Person p);

}
