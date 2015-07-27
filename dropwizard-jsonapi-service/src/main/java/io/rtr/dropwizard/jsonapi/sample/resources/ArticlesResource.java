package io.rtr.dropwizard.jsonapi.sample.resources;

import io.rtr.dropwizard.jsonapi.sample.core.DataStore;
import io.rtr.dropwizard.jsonapi.sample.models.Article;
import io.rtr.jsonapi.annotation.ApiResource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApiResource(model = Article.class)
@Path("articles")
@Produces({ "application/vnd.api+json", MediaType.APPLICATION_JSON })
@Consumes({ "application/vnd.api+json", MediaType.APPLICATION_JSON })
public class ArticlesResource {

  @Inject
  private DataStore store;

  @GET
  public Response getArticles() {
    return Response.ok(store.getArticles()).build();
  }

  @GET
  @Path("{id}")
  public Response getArticle(@PathParam("id") final String id) {
    return Response.ok(store.getArticle(id)).build();
  }

  @GET
  @Path("{id}/author")
  public Response getAuthor(@PathParam("id") final String id) {
    return Response.ok(store.getAuthor(id)).build();
  }
}
