package io.rtr.jsonapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {
  private final String type;
  private final String id;

  @JsonCreator
  public Data(@JsonProperty("type") String type, @JsonProperty("id") String id) {
    super();
    this.type = type;
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public String getId() {
    return id;
  }
}
