package io.rtr.jsonapi.util;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class FieldUtil {

  /**
   * Finds the field with the given name through the class's hierarchy. It will return the field that is closest in ancestry to the given class. That
   * is, if the field exists in both the parent class and the grandparent class, it will return the field that is in the parent class.
   *
   * @param obj
   *          Object that will be the starting point to search for the field
   * @param fieldName
   *          The name of the field to be searched for
   * @return The Field that corresponds to the fieldName
   * @throws NoSuchFieldException
   *           If the field is not found
   */
  public static Field findDeclaredField(final Object obj, final String fieldName) throws NoSuchFieldException {
    final List<Field> fields = Lists.newArrayList();
    getAllFields(fields, obj.getClass());
    for (final Field field : fields) {
      if (field.getName().equals(fieldName)) {
        return field;
      }
    }
    throw new NoSuchFieldException("No Field found for fieldName=" + fieldName + ", Obj=" + obj.getClass().getCanonicalName());
  }

  /**
   * A recursive method to find all the fields (including fields of the class's parents) of a class.
   *
   * @param fields
   *          A container for Field objects, which is used by the recursion
   * @param type
   *          The class that the recursion starts with
   * @return a list of fields from the given class and all of its parents
   */
  public static List<Field> getAllFields(List<Field> fields, final Class<?> type) {
    fields.addAll(Arrays.asList(type.getDeclaredFields()));

    if (type.getSuperclass() != null) {
      fields = getAllFields(fields, type.getSuperclass());
    }

    return fields;
  }
}
