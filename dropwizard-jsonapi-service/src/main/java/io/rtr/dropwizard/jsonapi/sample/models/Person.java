package io.rtr.dropwizard.jsonapi.sample.models;

import io.rtr.jsonapi.annotation.ApiModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@ApiModel(type = "people", id = "id")
@JsonInclude(Include.NON_NULL)
public class Person extends BaseModel {

  private String name;
  private String company;

  public Person() {}
  
  public Person(Person other) {
    this.name = other.name;
    this.company = other.company;
    setId(other.getId());
    setCreated(other.getCreated());
  }
  
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(final String company) {
    this.company = company;
  }

}