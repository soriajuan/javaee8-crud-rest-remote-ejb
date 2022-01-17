package person.web.api;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import person.common.pojo.Person;
import person.common.service.PersonService;

@Path("person")
@RequestScoped
public class PersonController {

	@EJB(lookup = "java:global/person-backend-ear/person-ejb/PersonBean!person.common.service.PersonService")
	private PersonService personService;

	@Inject
	private PersonMapper mapper;

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response put(@PathParam("id") int id, PersonPatchRequestPayload payload) {

		// Not very elegant but better than @NotNull leaking data in response body or
		// filter with a cumbersome implementation
		if (Objects.isNull(payload)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Person updated = personService.update(id, mapper.toPerson(payload));
		PersonGetResponsePayload responsePayload = mapper.toPersonGetResponsePayload(updated);

		return Response.ok().entity(responsePayload).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") int id) {
		return Response.ok().entity(mapper.toPersonGetResponsePayload(personService.findById(id))).build();
	}

	@DELETE
	@Path("/{id}")
	public void deleteById(@PathParam("id") int id) {
		personService.deleteById(id);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		List<PersonGetResponsePayload> all = personService.findAll().stream().map(d -> mapper.toPersonGetResponsePayload(d))
				.collect(Collectors.toList());
		return Response.ok().entity(all).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response post(PersonPostRequestPayload payload) {

		// Not very elegant but better than @NotNull leaking data in response body or
		// filter with a cumbersome implementation
		if (Objects.isNull(payload)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Person inserted = personService.insert(mapper.toPerson(payload));
		PersonGetResponsePayload responsePayload = mapper.toPersonGetResponsePayload(inserted);

		return Response.created(URI.create(String.format("/%d", responsePayload.getId()))).entity(responsePayload)
				.build();
	}

}
