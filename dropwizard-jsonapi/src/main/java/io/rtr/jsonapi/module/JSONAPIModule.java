package io.rtr.jsonapi.module;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JSONAPIModule extends SimpleModule {

  public JSONAPIModule() {
    super();
  }

  @Override
  public String getModuleName() {
    return "JSON API Module";
  }

  @Override
  public void setupModule(final SetupContext context) {

  }

  @Override
  public Version version() {
    return new Version(0, 0, 1, "SNAPSHOT", "io.rtr.jsonapi", "jsonapi");
  }

}
