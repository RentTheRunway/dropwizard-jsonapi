package io.rtr.dropwizard.jsonapi.sample.resources;

import io.rtr.dropwizard.jsonapi.sample.core.DataStore;
import io.rtr.dropwizard.jsonapi.sample.models.Person;
import io.rtr.jsonapi.annotation.ApiResource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApiResource(model = Person.class)
@Path("people")
@Produces({ "application/vnd.api+json", MediaType.APPLICATION_JSON })
@Consumes({ "application/vnd.api+json", MediaType.APPLICATION_JSON })
public class PeopleResource {

  @Inject
  private DataStore store;

  @GET
  public Response getPeople() {
    return Response.ok(store.getPeople()).build();
  }

  @POST
  public Person postPerson(Person person) {
    return store.addPerson(person.getId(), person.getName(), person.getCompany());
  }

  @DELETE
  @Path("{id}")
  public void deletePerson(@PathParam("id") final String id) {
    store.removePerson(id);
  }

  @GET
  @Path("{id}")
  public Response getPerson(@PathParam("id") final String id) {
    return Response.ok(store.getPerson(id)).build();
  }

  @GET
  @Path("{id}/articles")
  public Response getArticles(@PathParam("id") final String id) {
    return Response.ok(store.getArticlesByAuthor(id)).build();
  }
}
