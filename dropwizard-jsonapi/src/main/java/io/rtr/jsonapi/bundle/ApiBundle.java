package io.rtr.jsonapi.bundle;

import io.rtr.jsonapi.annotation.FieldFilterMixIn;
import io.rtr.jsonapi.filter.JsonApiFeature;
import io.rtr.jsonapi.filter.JsonApiMessageBodyWriter;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ApiBundle implements Bundle {

  @Override
  public void initialize(final Bootstrap<?> bootstrap) {
    bootstrap.getObjectMapper().addMixInAnnotations(Object.class, FieldFilterMixIn.class);
  }

  @Override
  public void run(final Environment environment) {
    environment.jersey().packages("io.rtr.jsonapi.filter");
    environment.jersey().register(JsonApiFeature.class);
    // Is this even necessary? It only adds the mixin that's added above.
    environment.jersey().register(JsonApiMessageBodyWriter.class);
  }

}
