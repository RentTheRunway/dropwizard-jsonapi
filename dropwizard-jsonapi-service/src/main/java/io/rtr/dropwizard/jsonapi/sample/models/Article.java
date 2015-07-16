package io.rtr.dropwizard.jsonapi.sample.models;

import io.rtr.jsonapi.annotation.ApiModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@ApiModel("articles")
@JsonInclude(Include.NON_NULL)
public class Article {
  private String id;
  private String title;
  private String body;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(final String body) {
    this.body = body;
  }
}