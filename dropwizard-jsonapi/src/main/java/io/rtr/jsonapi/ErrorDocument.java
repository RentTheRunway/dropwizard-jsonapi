package io.rtr.jsonapi;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class ErrorDocument {
  private final List<Error> errors;

  public ErrorDocument(final Error... errors) {
    this.errors = Lists.newArrayList(errors);
  }

  public List<Error> getErrors() {
    return Collections.unmodifiableList(this.errors);
  }
}
