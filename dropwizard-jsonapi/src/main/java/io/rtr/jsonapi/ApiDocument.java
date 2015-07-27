package io.rtr.jsonapi;

import java.util.List;
import java.util.Map;

public interface ApiDocument {
  Object getData();

  default List<Object> getIncluded() {
    return null;
  }

  default Map<String, Object> getLinks() {
    return null;
  }

  default Object getMeta() {
    return null;
  }
}
