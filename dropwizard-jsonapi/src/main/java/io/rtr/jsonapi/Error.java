package io.rtr.jsonapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class Error {

  private String id;
  private String href;
  private String status;
  private String code;
  private String title;
  private String detail;
  private List<String> links;
  private List<String> paths;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getHref() {
    return href;
  }

  public void setHref(final String href) {
    this.href = href;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(final String detail) {
    this.detail = detail;
  }

  public List<String> getLinks() {
    return links;
  }

  public void setLinks(final List<String> links) {
    this.links = links;
  }

  public List<String> getPaths() {
    return paths;
  }

  public void setPaths(final List<String> paths) {
    this.paths = paths;
  }
}
