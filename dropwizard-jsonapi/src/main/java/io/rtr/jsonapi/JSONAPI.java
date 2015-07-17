package io.rtr.jsonapi;

import io.rtr.jsonapi.annotation.ApiModel;
import io.rtr.jsonapi.impl.ApiDocumentImpl;
import io.rtr.jsonapi.impl.IncludesResourceObjectImpl;
import io.rtr.jsonapi.impl.ResourceObjectImpl;
import io.rtr.jsonapi.util.EntityUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

public class JSONAPI {

  public static ApiDocumentBuilder document(final ResourceObjectImpl data) {
    return new ApiDocumentBuilder(data);
  }

  public static ApiDocumentBuilder document(final List<ResourceObjectImpl> data) {
    return new ApiDocumentBuilder(data);
  }

  public static ResourceObjectBuilder data(final ResponseData data) {
    return new ResourceObjectBuilder(data);
  }

  public static IncludesResourceObjectBuilders includesData(final ResponseData data) {
    return new IncludesResourceObjectBuilders(data);
  }

  public static class ApiDocumentBuilder {
    private ResourceObjectImpl data;
    private List<ResourceObjectImpl> dataList;

    private final List<Object> includes = Lists.newLinkedList();
    private final Map<String, Object> links = Maps.newHashMap();
    private Object meta;

    public ApiDocumentBuilder(final ResourceObjectImpl data) {
      this.data = data;
    }

    public ApiDocumentBuilder(final List<ResourceObjectImpl> data) {
      this.dataList = data;
    }

    public ApiDocumentBuilder include(final Object include) {
      includes.add(include);
      return this;
    }

    public ApiDocumentBuilder link(final String key, final String link) {
      links.put(key, link);
      return this;
    }

    public ApiDocumentBuilder link(final String key, final JsonLink link) {
      links.put(key, link);
      return this;
    }

    public ApiDocumentBuilder meta(final Object meta) {
      this.meta = meta;
      return this;
    }

    public ApiDocument build(final UriInfo uriInfo) {
      final ApiDocumentImpl doc = new ApiDocumentImpl();
      if (data != null) {
        doc.setData(data);
      } else {
        doc.setData(dataList);
      }

      if (!includes.isEmpty()) {
        doc.setIncluded(includes);
      }
      if (!links.isEmpty()) {
        doc.setLinks(links);
      }
      if (meta != null) {
        doc.setMeta(meta);
      }
      return doc;
    }
  }

  public static class IncludesResourceObjectBuilders {
    private final ResponseData data;
    private final List<Object> includes = Lists.newLinkedList();
    private final Map<String, Object> links = Maps.newHashMap();
    private Object meta;

    public IncludesResourceObjectBuilders(final ResponseData data) {
      this.data = data;
    }

    public IncludesResourceObjectBuilders include(final Object include) {
      includes.add(include);
      return this;
    }

    public IncludesResourceObjectBuilders link(final String key, final String link) {
      links.put(key, link);
      return this;
    }

    public IncludesResourceObjectBuilders link(final String key, final JsonLink link) {
      links.put(key, link);
      return this;
    }

    public IncludesResourceObjectBuilders meta(final Object meta) {
      this.meta = meta;
      return this;
    }

    public IncludesResourceObjectImpl build() {
      final IncludesResourceObjectImpl doc = new IncludesResourceObjectImpl();
      doc.setData(data);
      doc.setType(EntityUtil.getType(data.attributes));

      if (!includes.isEmpty()) {
        doc.setIncluded(includes);
      }
      if (!links.isEmpty()) {
        doc.setLinks(links);
      }
      if (meta != null) {
        doc.setMeta(meta);
      }
      return doc;
    }
  }

  public static class ResourceObjectBuilder {
    private final ResponseData data;
    private final List<Object> includes = Lists.newLinkedList();
    private final Map<String, Object> links = Maps.newHashMap();
    private Object meta;

    public ResourceObjectBuilder(final ResponseData data) {
      this.data = data;
    }

    public ResourceObjectBuilder include(final Object include) {
      includes.add(include);
      return this;
    }

    public ResourceObjectBuilder link(final String key, final String link) {
      links.put(key, link);
      return this;
    }

    public ResourceObjectBuilder link(final String key, final JsonLink link) {
      links.put(key, link);
      return this;
    }

    public ResourceObjectBuilder meta(final Object meta) {
      this.meta = meta;
      return this;
    }

    public ResourceObjectImpl build() {
      final ResourceObjectImpl doc = new ResourceObjectImpl();
      doc.setType(getType(data.attributes));
      if (data.isEmpty()) {
        data.attributes = null;
      }
      doc.setData(data);
      doc.setType(getType(data.attributes));

      if (!includes.isEmpty()) {
        doc.setIncluded(includes);
      }
      if (!links.isEmpty()) {
        doc.setRelationships(links);
      }
      if (meta != null) {
        doc.setMeta(meta);
      }
      return doc;
    }

    private String getType(final Object data) {
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
}
