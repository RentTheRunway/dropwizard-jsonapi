package io.rtr.jsonapi.util;

import io.rtr.jsonapi.annotation.ApiModel;

public class EntityUtil {

  public static String getType(final Object data) {
    final ApiModel model = data.getClass().getAnnotation(ApiModel.class);
    if (model != null) {
      String type = model.value();
      if ("undefined".equals(type)) {
        type = model.type();
      }
      if ("undefined".equals(type)) {
        return null;
      }
      return type;
    }
    return null;
  }
}
