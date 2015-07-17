package io.rtr.jsonapi.filter;

import io.rtr.jsonapi.annotation.ApiModel;
import io.rtr.jsonapi.filter.mapping.ResourceMappingContext;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class ApiPropertyFilter implements PropertyFilter {
  private static final Logger log = LoggerFactory.getLogger(ApiPropertyFilter.class);

  private final Map<Class<?>, Set<String>> filtered = Maps.newHashMap();
  private Map<String, Set<String>> filtersByModelType = Maps.newHashMap();

  private final ResourceMappingContext mapping;

  public ApiPropertyFilter(final ResourceMappingContext mapping) {
    this.mapping = mapping;
  }

  public ApiPropertyFilter(final ResourceMappingContext mapping, final Map<String, Set<String>> filters) {
    this.mapping = mapping;
    this.filtersByModelType = filters;
    for (final Set<String> values : filtersByModelType.values()) {
      values.add("id");
      values.add("type");
    }
  }

  @Override
  public void serializeAsField(final Object pojo, final JsonGenerator jgen, final SerializerProvider prov, final PropertyWriter writer)
      throws Exception {
    if (pojo == null) {
      return;
    }

    final Class<?> declaringClass = pojo.getClass();
    final String modelType = getModelType(declaringClass);
    final Set<String> filteredFields = filtersByModelType.get(modelType);
    if (filteredFields != null && !filteredFields.contains(writer.getName())) {
      log.debug("filtered {}.{}", modelType, writer.getName());
      return;
    }
    writer.serializeAsField(pojo, jgen, prov);
  }

  private String getModelType(final Class<?> type) {
    final ApiModel model = type.getAnnotation(ApiModel.class);
    if (model != null) {
      String modelType = model.type();
      if ("undefined".equals(modelType)) {
        modelType = model.value();
      }
      return modelType;
    }
    return null;
  }

  @Override
  public void serializeAsElement(final Object elementValue, final JsonGenerator jgen, final SerializerProvider prov, final PropertyWriter writer)
      throws Exception {
    writer.serializeAsElement(elementValue, jgen, prov);
  }

  @Override
  public void depositSchemaProperty(final PropertyWriter writer, final ObjectNode propertiesNode, final SerializerProvider provider)
      throws JsonMappingException {
  }

  @Override
  public void depositSchemaProperty(final PropertyWriter writer, final JsonObjectFormatVisitor objectVisitor, final SerializerProvider provider)
      throws JsonMappingException {
  }

}
