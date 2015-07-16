package io.rtr.jsonapi.filter.mapping;

import io.rtr.jsonapi.annotation.ApiResource;

import com.google.common.collect.Maps;

import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ExtendedResourceContext;
import org.glassfish.jersey.server.model.AbstractResourceModelVisitor;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.server.model.ResourceModel;
import org.glassfish.jersey.server.model.ResourceModelComponent;
import org.glassfish.jersey.uri.PathPattern;
import org.glassfish.jersey.uri.UriTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Context;

public class ResourceMappingContext {
  private static final Logger log = LoggerFactory.getLogger(ResourceMappingContext.class);
  private final ExtendedResourceContext erc;

  private Map<Class<?>, Mapping> mappings;
  private Map<Class<?>, Mapping> mappingsByModel;

  public ResourceMappingContext(@Context final ExtendedResourceContext erc) {
    this.erc = erc;
  }

  public Mapping getMapping(final Class<?> resource) {
    buildMappings();
    return mappings.get(resource);
  }

  public Mapping getMapping(final String resourceName) {
    buildMappings();
    final Class<?> resource = getClassByName(resourceName);
    if (resource == null) {
      return null;
    }
    return mappings.get(resource);
  }

  public Mapping getMappingByModel(final Class<?> model) {
    buildMappings();
    return mappingsByModel.get(model);
  }

  private Class<?> getClassByName(final String resourceName) {
    try {
      return Class.forName(resourceName);
    } catch (final ClassNotFoundException e) {
      log.warn("Couldn't find resource class {}", resourceName);
      return null;
    }
  }

  private void buildMappings() {
    if (mappings != null) {
      return;
    }
    mappings = new HashMap<>();
    mappingsByModel = new HashMap<>();

    erc.getResourceModel().accept(new AbstractResourceModelVisitor() {

      Deque<PathPattern> stack = new LinkedList<>();

      private void processComponents(final ResourceModelComponent component) {

        final List<? extends ResourceModelComponent> components = component.getComponents();
        if (components != null) {
          for (final ResourceModelComponent rc : components) {
            rc.accept(this);
          }
        }
      }

      /*
       * This is the entire resource model for the application. Contains the collection of root resources (non-Javadoc)
       * 
       * @see org.glassfish.jersey.server.model.AbstractResourceModelVisitor#visitResourceModel(org.glassfish.jersey.server.model.ResourceModel)
       */
      @Override
      public void visitResourceModel(final ResourceModel resourceModel) {
        log.trace("Resource Model: {}", resourceModel);
        processComponents(resourceModel);
      }

      /*
       * Gets called for every root resource (non-Javadoc)
       * 
       * @see org.glassfish.jersey.server.model.AbstractResourceModelVisitor#visitResource(org.glassfish.jersey.server.model.Resource)
       */
      @Override
      public void visitResource(final Resource resource) {
        log.trace("Resource: {}", resource.getPath());
        visitResourceIntl(resource, true);
      }

      /*
       * Called for every child of a top level resource. These are the actual resource paths in the root level resource. (non-Javadoc)
       * 
       * @see org.glassfish.jersey.server.model.AbstractResourceModelVisitor#visitChildResource(org.glassfish.jersey.server.model.Resource)
       */
      @Override
      public void visitChildResource(final Resource resource) {
        log.debug("Child Resource: {}", resource.getPath());
        log.debug("Parent: {}", resource.getParent());
        log.debug("Path: {}", resource.getPath());
        log.debug("Template Var: {}", resource.getPathPattern().getTemplate().getTemplateVariables());
        final UriTemplate template = resource.getPathPattern().getTemplate();
        if (template.getNumberOfTemplateVariables() == 1) {
          final Class root = getLikelyRoot(resource.getParent());
          if (root != null) {
            addMappingPath(root, resource.getPath());
            for (final ResourceMethod method : resource.getAllMethods()) {
              if ("GET".equals(method.getHttpMethod())) {
                addMappingMethod(root, resource.getPath(), method.getInvocable().getDefinitionMethod());
              }
            }
          }
        }
        visitResourceIntl(resource, false);
      }

      /*
       * Called for each method in a resource. Includes the actual method that will be called to handle a request.
       * 
       * (non-Javadoc)
       * 
       * @see org.glassfish.jersey.server.model.AbstractResourceModelVisitor#visitResourceMethod(org.glassfish.jersey.server.model.ResourceMethod)
       */
      @Override
      public void visitResourceMethod(final ResourceMethod resourceMethod) {
        log.trace("Resource Method: {}", resourceMethod);
        if (resourceMethod.isExtended()) {
          return;
        }
        log.debug("Method definition: {}", resourceMethod.getInvocable().getDefinitionMethod());

        if (ResourceMethod.JaxrsType.SUB_RESOURCE_LOCATOR.equals(resourceMethod.getType())) {
          if (resourceMethod.getInvocable() != null) {
            final Invocable i = resourceMethod.getInvocable();

            final Type type = i.getResponseType();
            final String template = getTemplate();

            addMappingRoot((Class) type, template);

            // Process sub resources ?

            Resource.Builder builder = Resource.builder(i.getRawResponseType());
            if (builder == null) {
              // for example in the case the return type of the sub resource locator is Object
              builder = Resource.builder().path(resourceMethod.getParent().getPath());
            }
            final Resource subResource = builder.build();

            visitChildResource(subResource);
          }
        }

        processComponents(resourceMethod);
      }

      private Class getLikelyRoot(final Resource resource) {
        Class likelyToBeRoot = null;
        for (final Class next : resource.getHandlerClasses()) {
          if (!(Inflector.class.isAssignableFrom(next))) {
            likelyToBeRoot = next;
          }
        }
        return likelyToBeRoot;
      }

      private void visitResourceIntl(final Resource resource, final boolean isRoot) {
        try {
          stack.addLast(resource.getPathPattern());
          processComponents(resource);

          if (isRoot) {
            final Class likelyToBeRoot = getLikelyRoot(resource);

            if (likelyToBeRoot != null) {
              addMappingRoot(likelyToBeRoot, getTemplate());
            }
          }
        } finally {
          stack.removeLast();
        }
      }

      private void addMappingRoot(final Class type, final String rootTemplate) {
        Mapping mapping = mappings.get(type);
        if (mapping == null) {
          mapping = new Mapping();
          mappings.put(type, mapping);
        }
        mapping.setRootPathTemplate(rootTemplate);

        final Class modelClass = getModelClass(type);
        if (modelClass != null && !mappingsByModel.containsKey(modelClass)) {
          mappingsByModel.put(modelClass, mapping);
        }
      }

      private void addMappingPath(final Class type, final String template) {
        Mapping mapping = mappings.get(type);
        if (mapping == null) {
          mapping = new Mapping();
          mappings.put(type, mapping);
        }
        mapping.addPathTemplate(template);

        final Class modelClass = getModelClass(type);
        if (modelClass != null && !mappingsByModel.containsKey(modelClass)) {
          mappingsByModel.put(modelClass, mapping);
        }
      }

      private void addMappingMethod(final Class type, final String template, final Method method) {
        Mapping mapping = mappings.get(type);
        if (mapping == null) {
          mapping = new Mapping();
          mappings.put(type, mapping);
        }
        mapping.addPathMethod(template, method);

        final Class modelClass = getModelClass(type);
        if (modelClass != null && !mappingsByModel.containsKey(modelClass)) {
          mappingsByModel.put(modelClass, mapping);
        }
      }

      private Class getModelClass(final Class<?> resourceClass) {
        final ApiResource resource = resourceClass.getDeclaredAnnotation(ApiResource.class);
        if (resource == null) {
          return null;
        }
        return resource.model();
      }

      private String getTemplate() {
        final StringBuilder template = new StringBuilder();
        for (final PathPattern pp : stack) {
          final String ppTemplate = pp.getTemplate().getTemplate();

          final int tlength = template.length();
          if (tlength > 0) {
            if (template.charAt(tlength - 1) == '/') {
              if (ppTemplate.startsWith("/")) {
                template.append(ppTemplate, 1, ppTemplate.length());
              } else {
                template.append(ppTemplate);
              }
            } else {
              if (ppTemplate.startsWith("/")) {
                template.append(ppTemplate);
              } else {
                template.append("/");
                template.append(ppTemplate);
              }
            }
          } else {
            template.append(ppTemplate);
          }

        }
        return template.toString();
      }
    });

  }

  public static class Mapping {
    private String rootPath;
    private final Map<String, String> paths = Maps.newHashMap();
    private final Map<String, Method> pathMethods = Maps.newHashMap();

    public Mapping() {
    }

    public String getRootPathTemplate() {
      return rootPath;
    }

    public void setRootPathTemplate(final String root) {
      rootPath = root;
    }

    public String getPathTemplate(final String key) {
      final String path = paths.get(key);
      if (path == null) {
        return null;
      }
      String seperator = "/";
      if (path.startsWith("/")) {
        seperator = "";
      }
      return rootPath + seperator + path;
    }

    public Method getPathMethod(final String key) {
      return pathMethods.get(key);
    }

    public Object getValue(final Object resource, final String key, final Object... args) {
      final Method accessor = pathMethods.get(key);
      if (accessor == null) {
        return null;
      }

      try {
        return accessor.invoke(resource, args);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        log.warn("Failed to invoke resource method.", e);
      }

      return null;
    }

    public void addPathTemplate(final String key, final String path) {
      paths.put(key, path);
    }

    public void addPathTemplate(final String path) {
      String key = "self";
      final int index = path.lastIndexOf('/');
      if (index > 0 && index < path.length() - 1) {
        key = path.substring(path.lastIndexOf('/') + 1);
      }
      paths.put(key, path);
    }

    public void addPathMethod(final String path, final Method method) {
      String key = "self";
      final int index = path.lastIndexOf('/');
      if (index > 0 && index < path.length() - 1) {
        key = path.substring(path.lastIndexOf('/') + 1);
      }
      pathMethods.put(key, method);
    }

    public Iterable<String> getKeys() {
      return paths.keySet();
    }

    @Override
    @SuppressWarnings("checkstyle:indentation")
    public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("{root:");
      sb.append(rootPath);

      paths.forEach((k, v) -> {
        sb.append(", ");
        sb.append(k);
        sb.append(":");
        sb.append(v);
      });
      sb.append("}");
      return sb.toString();
    }
  }
}
