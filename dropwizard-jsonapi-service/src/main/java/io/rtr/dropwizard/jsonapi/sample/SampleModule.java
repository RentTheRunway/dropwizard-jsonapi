package io.rtr.dropwizard.jsonapi.sample;

import io.rtr.dropwizard.jsonapi.sample.core.DataStore;

import io.dropwizard.setup.Environment;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class SampleModule extends AbstractBinder {
  private final SampleConfiguration config;
  private final Environment env;

  public SampleModule(final SampleConfiguration config, final Environment env) {
    this.config = config;
    this.env = env;
  }

  @Override
  protected void configure() {
    bindAsContract(DataStore.class).in(Singleton.class);
  }

}
