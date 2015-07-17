package io.rtr.dropwizard.jsonapi.sample;

import io.rtr.dropwizard.jsonapi.sample.health.SampleHealthCheck;
import io.rtr.dropwizard.jsonapi.sample.resources.ArticlesResource;
import io.rtr.dropwizard.jsonapi.sample.resources.PeopleResource;
import io.rtr.jsonapi.bundle.ApiBundle;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SampleService extends Application<SampleConfiguration> {

  private final Class<?>[] RESOURCES = { ArticlesResource.class, PeopleResource.class };
  private final Class<?>[] MANAGED = {};

  @Override
  public String getName() {
    return "Dropwizard JSONAP Sample";
  }

  @Override
  public void initialize(final Bootstrap<SampleConfiguration> bootstrap) {
    bootstrap.addBundle(new ApiBundle());
  }

  @Override
  public void run(final SampleConfiguration configuration, final Environment environment) throws Exception {
    environment.jersey().register(new SampleModule(configuration, environment));

    for (final Class<?> c : MANAGED) {
      environment.jersey().register(c);
    }
    for (final Class<?> c : RESOURCES) {
      environment.jersey().register(c);
    }

    final SampleHealthCheck health = new SampleHealthCheck();
    environment.healthChecks().register(SampleHealthCheck.class.getName(), health);
  }

  public static void main(final String[] args) throws Exception {
    new SampleService().run(args);
  }

}
