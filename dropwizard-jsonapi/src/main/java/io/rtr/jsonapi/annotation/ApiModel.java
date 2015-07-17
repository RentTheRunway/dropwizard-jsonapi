package io.rtr.jsonapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ApiModel {
  /**
   * Alias for the type.
   */
  public String value() default "undefined";

  /**
   * The model type. It MUST match the resource name.
   */
  public String type() default "undefined";

  /**
   * The id field in the model. Defaults to id.
   */
  public String id() default "id";
}
