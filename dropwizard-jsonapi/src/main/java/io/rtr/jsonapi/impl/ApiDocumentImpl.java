package io.rtr.jsonapi.impl;

import io.rtr.jsonapi.ApiDocument;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class ApiDocumentImpl implements ApiDocument {

  private Object data;
  private Object meta;
  private List<Object> included;
  private Map<String, Object> links;

  @Override
  public Object getData() {
    return data;
  }

  @Override
  public Object getMeta() {
    return meta;
  }

  @Override
  public List<Object> getIncluded() {
    return included;
  }

  @Override
  public Map<String, Object> getLinks() {
    return links;
  }

  public void setData(final Object data) {
    this.data = data;
  }

  public void setMeta(final Object meta) {
    this.meta = meta;
  }

  public void setIncluded(final List<Object> included) {
    this.included = included;
  }

  public void setLinks(final Map<String, Object> links) {
    this.links = links;
  }
}
