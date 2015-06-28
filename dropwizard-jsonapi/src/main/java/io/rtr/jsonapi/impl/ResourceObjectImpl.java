package io.rtr.jsonapi.impl;

import io.rtr.jsonapi.ResponseData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class ResourceObjectImpl {
  @JsonUnwrapped
  private ResponseData data;
  private String type;

  private Object meta;
  private List<Object> included;
  private Map<String, Object> relationships;

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public ResponseData getData() {
    return data;
  }

  public void setData(final ResponseData data) {
    this.data = data;
  }

  public Object getMeta() {
    return meta;
  }

  public void setMeta(final Object meta) {
    this.meta = meta;
  }

  public List<Object> getIncluded() {
    return included;
  }

  public void setIncluded(final List<Object> included) {
    this.included = included;
  }

  public Map<String, Object> getRelationships() {
    return relationships;
  }

  public void setRelationships(final Map<String, Object> relationships) {
    this.relationships = relationships;
  }
}
