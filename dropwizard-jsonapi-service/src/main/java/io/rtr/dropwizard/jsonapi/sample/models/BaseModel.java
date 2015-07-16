package io.rtr.dropwizard.jsonapi.sample.models;

public abstract class BaseModel {

  private String id;
  private String created;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(final String created) {
    this.created = created;
  }

}
