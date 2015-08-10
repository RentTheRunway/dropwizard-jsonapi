package io.rtr.jsonapi;

import io.rtr.jsonapi.util.FieldUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResponseData {
  private static final Logger log = LoggerFactory.getLogger(ResponseData.class);
  String id;
  Object attributes;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public Object getAttributes() {
    setIdAttribute(attributes);
    return attributes;
  }

  public void setAttributes(final Object attributes) {
    this.attributes = attributes;
  }

  private void setIdAttribute(final Object obj) {
    try {
      final Field field = FieldUtil.findDeclaredField(obj, "id");
      field.setAccessible(true);
      field.set(obj, null);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | NullPointerException e) {
      //catch a NPE here if we have a null attributes section on purpose due to no values in the attributes section
      log.debug("exception while setting Id attribute to null: {}", e);
    }
  }

  @JsonIgnore
  public Boolean isEmpty() {
    List<Field> fields = new ArrayList<>();
    FieldUtil.getAllFields(fields, attributes.getClass());
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        if (field.get(attributes) != null) {
          return false;
        }
      } catch (IllegalAccessException e) {
        //we set access to true, so this shouldn't be possible to hit
        log.warn("Hit exception: {}", e);
      }
    }
    return true;
  }


}
