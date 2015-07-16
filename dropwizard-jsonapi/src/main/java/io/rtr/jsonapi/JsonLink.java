package io.rtr.jsonapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class JsonLink {

  private final String self;
  private final String related;
  private final List<Data> data;
  private final Object meta;

  @JsonCreator
  public JsonLink(@JsonProperty("self") String self, @JsonProperty("related") String related, @JsonProperty("data") List<Data> data,
      @JsonProperty("meta") Object meta) {
    super();
    this.self = self;
    this.related = related;
    this.data = data;
    this.meta = meta;
  }

  public JsonLink(String self, String related, List<Data> data) {
    this(self, related, data, null);
  }

  public String getSelf() {
    return self;
  }

  public String getRelated() {
    return related;
  }

  public List<Data> getData() {
    return data;
  }

  public Object getMeta() {
    return meta;
  }
}
